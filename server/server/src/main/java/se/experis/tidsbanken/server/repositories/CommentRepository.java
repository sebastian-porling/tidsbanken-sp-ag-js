package se.experis.tidsbanken.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.experis.tidsbanken.server.models.Comment;
import se.experis.tidsbanken.server.models.VacationRequest;

import java.util.List;
import java.util.Optional;

/**
 * Handles sql methods for Comments
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Finds all comments associated with vacation request and sorts them by created ascending
     * @param request Vacation Request
     * @return List of comments or empty
     */
    List<Comment> findAllByRequestOrderByCreatedAtAsc(VacationRequest request);

    /**
     * Finds all comments by vacation request
     * @param request Vacation Request
     * @return list of comments or empty
     */
    List<Comment> findAllByRequest(VacationRequest request);

    /**
     * Finds comment with id associated with provided vacation request
     * @param commentId Comment id
     * @param request Vacation Request
     * @return Optional with comment or empty
     */
    Optional<Comment> findByIdAndRequestOrderByCreatedAtAsc(Long commentId, VacationRequest request);
}
