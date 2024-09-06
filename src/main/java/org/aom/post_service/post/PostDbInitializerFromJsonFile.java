package org.aom.post_service.post;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
@ConditionalOnProperty(name = "db.load.json", havingValue = "true")
public class PostDbInitializerFromJsonFile implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(PostDbInitializerFromJsonFile.class);
    @Autowired
    private PostRepository postRepository;

    @Autowired
    ObjectMapper objectMapper;

    public PostDbInitializerFromJsonFile(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Attempting to read posts.json file to load into the database");
        List<Post> posts = objectMapper.readValue(new File("src/main/resources/posts.json"), new TypeReference<List<Post>>() {});
        postRepository.saveAll(posts);
        logger.info("posts.json file read and data loaded to the database");
    }
}
