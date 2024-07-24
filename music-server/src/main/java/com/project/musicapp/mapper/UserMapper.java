package mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import model.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
}
