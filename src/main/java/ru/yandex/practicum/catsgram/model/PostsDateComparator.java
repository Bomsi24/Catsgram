package ru.yandex.practicum.catsgram.model;

import java.util.Comparator;

public class PostsDateComparator implements Comparator<Post> {

    @Override
    public int compare(Post o1, Post o2) {
        return o1.getPostDate().compareTo(o2.getPostDate());
    }

    @Override
    public Comparator<Post> reversed() {
        return Comparator.super.reversed();
    }
}
