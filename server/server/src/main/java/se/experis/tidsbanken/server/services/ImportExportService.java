package se.experis.tidsbanken.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.experis.tidsbanken.server.models.*;
import se.experis.tidsbanken.server.repositories.*;
import se.experis.tidsbanken.server.socket.NotificationObserver;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImportExportService {
    @Autowired private VacationRequestRepository vrRepository;
    @Autowired private CommentRepository commentRepository;
    @Autowired private IneligiblePeriodRepository ipRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private StatusRepository statusRepository;
    @Autowired private NotificationObserver observer;

    @Transactional
    public List<ImportExportDTO> getExportData() {
        return vrRepository.findAll().stream()
                .map(this::toImportExportDTO)
                .collect(Collectors.toList());
    }

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

    private boolean notValidVacationRequest(VacationRequest vr) {
    return !vrRepository.findAllByOwner(vr.getOriginalOwner()).stream().allMatch(vr::excludesInPeriod)
                || !ipRepository.findAllByOrderByStartDesc().stream().allMatch(vr::excludesInIP);
    }

    private VacationRequest DTOtoVacationRequest(ImportExportDTO dto, User owner, User moderator) {
        final VacationRequest vr = new VacationRequest();
        vr.setTitle(dto.getTitle());
        vr.setStart(dto.getStart());
        vr.setEnd(dto.getEnd());
        vr.setOwner(owner);
        vr.setModerator(moderator);
        vr.setModerationDate(dto.getModerationDate());
        Optional.of(dto.getCreatedAt()).ifPresent(vr::setCreatedAt);
        Optional.of(dto.getModifiedAt()).ifPresent(vr::setModifiedAt);
        vr.setStatus(statusRepository.findByStatus(dto.getStatus()).orElseThrow());
        return vr;
    }

    private List<Comment> DTOtoComments(List<CommentDTO> commentDTOs, VacationRequest vr) {
        return commentDTOs.stream()
                .map(cDTO -> {
                    final Comment c = new Comment();
                    c.setMessage(cDTO.getMessage());
                    c.setUser(userRepository.findByIdAndIsActiveTrue(
                            cDTO.getUserId()).orElseThrow());
                    Optional.of(cDTO.getCreatedAt()).ifPresent(c::setCreatedAt);
                    Optional.of(cDTO.getModifiedAt()).ifPresent(c::setModifiedAt);
                    c.setRequest(vr);
                    return c;
                }).collect(Collectors.toList());
    }

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
