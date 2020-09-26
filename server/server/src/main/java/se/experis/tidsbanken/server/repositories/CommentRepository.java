package se.experis.tidsbanken.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.experis.tidsbanken.server.models.Comment;
import se.experis.tidsbanken.server.models.VacationRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByRequestOrderByCreatedAtDesc(VacationRequest request);

    List<Comment> findAllByRequest(VacationRequest request);

    Optional<Comment> findByIdAndRequestOrderByCreatedAtDesc(Long commentId, VacationRequest request);
}
