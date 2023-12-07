package kr.nadeuli.service.post;

import kr.nadeuli.entity.Member;
import kr.nadeuli.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.gu = :gu AND (p.title LIKE %:keyword% OR p.content LIKE %:keyword%)")
    Page<Post> findByGuAndTitleOrContentContaining(@Param("gu") String gu, @Param("keyword") String keyword, Pageable pageable);


}
