package se.experis.tidsbanken.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.experis.tidsbanken.server.models.*;
import se.experis.tidsbanken.server.repositories.*;
import se.experis.tidsbanken.server.socket.NotificationObserver;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImportExportService {
    @Autowired private VacationRequestRepository vrRepository;
    @Autowired private CommentRepository commentRepository;
    @Autowired private IneligiblePeriodRepository ipRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private NotificationObserver observer;

    @Transactional
    public List<ImportExportDTO> getExportData() {
        return vrRepository.findAll().stream()
                .map(this::toImportExportDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveImportData(List<ImportExportDTO> importData) {

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
