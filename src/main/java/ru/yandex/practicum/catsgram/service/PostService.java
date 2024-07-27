package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.SortOrder;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final Map<Long, Post> posts = new HashMap<>();
    private final UserService userService;

    @Autowired
    public PostService(UserService userService) {
        this.userService = userService;
    }

    public Optional<Post> findById(Long postId) {
        return posts.keySet().stream()
                .filter(p -> p.equals(postId))
                .findFirst()
                .map(posts::get);
    }

    public List<Post> findAll(String sort,
                                    int size,
                                    int from) {
        if (sort.equals("normal") && size < 1 && from < 1) {
            return posts.values().stream()
                    .sorted(Comparator.comparing(Post::getPostDate).reversed())// проверить
                    .limit(10)
                    .collect(Collectors.toList());
        }

        List<Post> postsColl = new ArrayList<>(posts.values());
        SortOrder sortColl = sorted(sort);
        if (sortColl != null) {
            if (sortColl == SortOrder.ASCENDING) {
                postsColl.sort(Comparator.comparing(Post::getPostDate));
            } else {
                postsColl.sort(Comparator.comparing(Post::getPostDate).reversed());
            }
        }

        return postsColl.stream()
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());


    }

    private static SortOrder sorted(String order) {
        switch (order.toLowerCase()) {
            case "ascending":
            case "asc":
                return SortOrder.ASCENDING;

            case "descending":
            case "desc":
                return SortOrder.DESCENDING;

            default:
                return null;
        }
    }

    public Post create(Post post) {
        // проверяем выполнение необходимых условий
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
        userService.getUserById(post.getAuthorId());
        // формируем дополнительные данные
        post.setId(getNextId());
        post.setPostDate(Instant.now());
        // сохраняем новую публикацию в памяти приложения
        posts.put(post.getId(), post);
        return post;
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }


    public Post update(Post newPost) {
        // проверяем необходимые условия
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            // если публикация найдена и все условия соблюдены, обновляем её содержимое
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }
}
