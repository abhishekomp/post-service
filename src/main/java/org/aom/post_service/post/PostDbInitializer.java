package org.aom.post_service.post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@ConditionalOnProperty(name = "db.load.json", havingValue = "false", matchIfMissing = true)
public class PostDbInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(PostDbInitializer.class);
    @Autowired
    private PostRepository postRepository;

    public PostDbInitializer(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Attempting to load the database with a few posts");
        Post post1 = new Post("USER1", "post1", "post body1", LocalDate.of(2024, 9, 1), false);
        Post post2 = new Post("USER2", "post2", "post body2", LocalDate.of(2024, 9, 10), false);
        Post post3 = new Post("USER1", "post3", "post body3", LocalDate.of(2024, 8, 1), true);
//        Post post4 = new Post("USER1", "post4 blocked", "post body4", LocalDate.of(2024, 5, 31), true);
//        Post post5 = new Post("USER2", "post5", "post body5", LocalDate.of(2023, 12, 31), false);
        postRepository.saveAll(List.of(post1, post2, post3));
        logger.info("Load the database with a few posts");
    }
}
