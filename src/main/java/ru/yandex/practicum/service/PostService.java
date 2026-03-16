package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.model.DTOObjects.CreatePostDTO;
import ru.yandex.practicum.model.DTOObjects.PageDTO;
import ru.yandex.practicum.model.DTOObjects.PostDTO;
import ru.yandex.practicum.model.DTOObjects.UpdatePostDTO;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.PostRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PageDTO getPosts(String search, Long pageNumber, Long pageSize) {
        List<Post> allPosts = postRepository.getPosts(search, pageNumber, pageSize);
        List<Post> posts = subList(allPosts, pageNumber, pageSize);
        int totalPosts = allPosts.size();
        int lastPage = (int) Math.ceil((double) totalPosts / pageSize);
        List<PostDTO> postDtos = posts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = pageNumber < lastPage;

        return new PageDTO(postDtos, hasPrev, hasNext, lastPage);
    }
    
    private List<Post> subList(List<Post> posts, Long pageNumber, Long pageSize){
        int totalPosts = posts.size();

        int startIndex = ((pageNumber.intValue() - 1) * pageSize.intValue());
        int endIndex = Math.min(startIndex + pageSize.intValue(), totalPosts);
        return startIndex < totalPosts? posts.subList(startIndex, endIndex) : posts;
    }

    public PostDTO getPost(Long id){
        return convertToDTO(postRepository.getPost(id));
    }

    public PostDTO createPost(CreatePostDTO createPostDTO) {
        Post post = new Post();
        post.setTitle(createPostDTO.getTitle());
        post.setText(createPostDTO.getText());
        post.setTagsList(createPostDTO.getTags());
        post.setLikesCount(0);
        post.setCommentsCount(0);

        Post savedPost = postRepository.savePost(post);
        return convertToDTO(savedPost);
    }
    public PostDTO updatePost(UpdatePostDTO updatePostDTO) {
        Post post = new Post();
        post.setId(updatePostDTO.getId());
        post.setTitle(updatePostDTO.getTitle());
        post.setText(updatePostDTO.getText());
        post.setTagsList(updatePostDTO.getTags());
        post.setLikesCount(0);
        post.setCommentsCount(0);

        Post savedPost = postRepository.savePost(post);
        return convertToDTO(savedPost);
    }
    public void deletePost(Long id) {
        postRepository.deletePost(id);
    }

    public int addLikes(Long id) {
        postRepository.addLikes(id);
        return postRepository.getLikes(id);
    }

    public void updateImage(Long id, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            postRepository.updateImage(id, image.getBytes());
        }
    }
    public byte[] getImage(Long id) {
        return postRepository.getImage(id);
    }


    private PostDTO convertToDTO(Post post) {
        return new PostDTO(
                post.getId(),
                post.getTitle(),
                post.getText(),
                post.getTagsList(),
                post.getLikesCount(),
                post.getCommentsCount()
        );
    }
}