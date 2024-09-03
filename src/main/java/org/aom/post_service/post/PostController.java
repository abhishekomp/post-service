package org.aom.post_service.post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post-api/")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    private final PostService postService;

    public PostController(PostService postService) {
        logger.info("PostController constructor was invoked");
        this.postService = postService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Post>> getAllPosts(){
        logger.info("PostController::getAllPosts() was invoked");
        List<Post> allPosts = postService.getAllPosts();
        return ResponseEntity.ok(allPosts);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<Post> getSinglePost(@PathVariable int id){
        logger.info("PostController::getSinglePost() was invoked");
        Post post = postService.findById(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/allByUserId")
    public ResponseEntity<List<Post>> getAllPostsByUserId(@RequestParam String userId){
        logger.info("PostController::getAllPosts() was invoked");
        List<Post> allPosts = postService.getAllPostsByUserId(userId);
        return ResponseEntity.ok(allPosts);
    }

    @PostMapping("createPost")
    @ResponseStatus(HttpStatus.CREATED)
    public Post createPost(@RequestBody Post post){
        return postService.createNewPost(post);
    }

    @PostMapping("createMultiplePost")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Post> createMultiplePost(@RequestBody List<Post> posts){
        return postService.createMultipleNewPosts(posts);
    }

    @PutMapping("updatePost/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Post updatePost(@RequestBody Post post, @PathVariable int id){
        return postService.updatePost(post, id);
    }
}
