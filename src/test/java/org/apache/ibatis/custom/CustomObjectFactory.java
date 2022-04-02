package org.apache.ibatis.custom;

import org.apache.ibatis.domain.blog.Post;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;

import java.util.UUID;

/**
 * @author ck163
 * @date 2022-03-31 17:23:54
 */
public class CustomObjectFactory extends DefaultObjectFactory {
    @Override
    public Object create(Class type) {
        if (type != null && type.equals(Post.class)) {
            Post post = (Post) super.create(type);
            post.setId(Integer.valueOf(UUID.randomUUID().toString()));
            return post;
        }
        return super.create(type);
    }
}
