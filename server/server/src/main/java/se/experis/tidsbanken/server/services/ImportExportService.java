package se.experis.tidsbanken.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.experis.tidsbanken.server.DTOs.CommentDTO;
import se.experis.tidsbanken.server.DTOs.ImportExportDTO;
import se.experis.tidsbanken.server.models.*;
import se.experis.tidsbanken.server.repositories.*;
import se.experis.tidsbanken.server.socket.NotificationObserver;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Handles the import and export of Vacation Request data
 * and comments associated with the Vacation Request
 */
@Service
public class ImportExportService {
    @Autowired private VacationRequestRepository vrRepository;
    @Autowired private CommentRepository commentRepository;
    @Autowired private IneligiblePeriodRepository ipRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private StatusRepository statusRepository;
    @Autowired private NotificationObserver observer;

    /**
     * Returns all Vacation Requests and comments as a combined object
     * @return list of Vacation Requests and its comments
     */
    @Transactional
    public List<ImportExportDTO> getExportData() {
        return vrRepository.findAll().stream()
                .map(this::toImportExportDTO)
                .collect(Collectors.toList());
    }

    /**
     * Saves data to the Vacation Request and Comment repository if it is valid
     * @param importData list of Vacation Request and comments
     * @throws Exception is thrown when a parsing error has occurred, or users doesn't exist or overlapping Vacation Requests
     */
    @Transactional
    public void saveImportData(List<ImportExportDTO> importData) throws Exception{
        for (ImportExportDTO ieDTO: importData) {
            final Optional<User> owner = userRepository.findByIdAndIsActiveTrue(ieDTO.getUserId());
            final Optional<User> moderator = userRepository.findByIdAndIsActiveTrue(ieDTO.getModeratorId());
            final VacationRequest vr = DTOtoVacationRequest(ieDTO, owner.orElseThrow(), moderator.orElse(null));
            if (notValidVacationRequest(vr)) throw new Exception("Overlaps other Vacation Requests or Ineligible Periods");
            final VacationRequest savedVr = vrRepository.save(vr);
            final List<Comment> comments = DTOtoComments(ieDTO.getComments(), savedVr);
            comments.forEach(c -> commentRepository.save(c.setRequest(savedVr)));
        }
    }

    /**
     * Checks so the Vacation Request doesn't overlap with existing user Vacation Request or Ineligible Periods
     * @param vr Vacation Request
     * @return true if not valid
     */
    private boolean notValidVacationRequest(VacationRequest vr) {
    return !vrRepository.findAllByOwner(vr.getOriginalOwner()).stream().allMatch(vr::excludesInPeriod)
                || !ipRepository.findAllByOrderByStartDesc().stream().allMatch(vr::excludesInIP);
    }

    /**
     * Mapps a DTO to a Vacation Request
     * @param dto ImportExportDTO
     * @param owner User who owns the Vacation Request
     * @param moderator User who moderated the Vacation Request or null if not moderated
     * @return Vacation Request
     */
    private VacationRequest DTOtoVacationRequest(ImportExportDTO dto, User owner, User moderator) {
        final VacationRequest vr = new VacationRequest();
        vr.setTitle(dto.getTitle());
        vr.setStart(dto.getStart());
        vr.setEnd(dto.getEnd());
        vr.setOwner(owner);
        vr.setModerator(moderator);
        vr.setModerationDate(dto.getModerationDate());
        Optional.ofNullable(dto.getCreatedAt()).ifPresent(vr::setCreatedAt);
        Optional.ofNullable(dto.getModifiedAt()).ifPresent(vr::setModifiedAt);
        vr.setStatus(statusRepository.findByStatus(dto.getStatus()).orElseThrow());
        return vr;
    }

    /**
     * Mapps CommentDTO to comment entity
     * @param commentDTOs list of comment dtos
     * @param vr Vacation Request that the comments is associated to
     * @return returns list of comments
     */
    private List<Comment> DTOtoComments(List<CommentDTO> commentDTOs, VacationRequest vr) {
        return commentDTOs.stream()
                .map(cDTO -> {
                    final Comment c = new Comment();
                    c.setMessage(cDTO.getMessage());
                    c.setUser(userRepository.findByIdAndIsActiveTrue(
                            cDTO.getUserId()).orElseThrow());
                    Optional.ofNullable(cDTO.getCreatedAt()).ifPresent(c::setCreatedAt);
                    Optional.ofNullable(cDTO.getModifiedAt()).ifPresent(c::setModifiedAt);
                    c.setRequest(vr);
                    return c;
                }).collect(Collectors.toList());
    }

    /**
     * Maps a Vacation Request to a DTO
     * @param vr Vacation Request
     * @return ImportExportDTO
     */
    private ImportExportDTO toImportExportDTO(VacationRequest vr) {
        final List<Comment> comments = commentRepository.findAllByRequest(vr);
        return new ImportExportDTO(
                vr.getTitle(),
                vr.getStart(),
                vr.getEnd(),
                vr.getOriginalOwner().getId(),
                vr.getStatus().getStatus(),
                vr.getCreatedAt(),
                vr.getModifiedAt(),
                vr.getOriginalModerator() != null
                        ? vr.getOriginalModerator().getId()
                        : null,
                vr.getModerationDate(),
                toCommentDTO(comments));
    }

    /**
     * Maps list of comments to a list of CommentDTOs
     * @param comments list of comments
     * @return list of CommentDTOs
     */
    private List<CommentDTO> toCommentDTO(List<Comment> comments) {
        return comments.stream()
                .map(c -> new CommentDTO(
                        c.getMessage(),
                        c.getOriginalUser().getId(),
                        c.getCreatedAt(),
                        c.getModifiedAt()))
                .collect(Collectors.toList());
    }
}
