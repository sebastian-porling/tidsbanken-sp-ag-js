package se.experis.tidsbanken.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.xml.stream.events.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment getById(long id);
}
