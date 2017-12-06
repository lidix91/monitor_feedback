package ch.fhnw.cere.repository.repositories;


import ch.fhnw.cere.repository.models.UserFBDislike;
import ch.fhnw.cere.repository.models.UserFBLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserFeedbackLikeRepository extends JpaRepository<UserFBLike, Long> {
    List<UserFBLike> findByFeedbackId(@Param("feedbackId") long feedbackId);
    List<UserFBLike> findByUserId(@Param("userId") long userId);
}
