package mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import model.domain.Comment;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentMapper extends BaseMapper<Comment> {
}
