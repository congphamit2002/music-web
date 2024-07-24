package model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName(value = "list_song")
@Data
public class ListSong {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer songId;

    private Integer songListId;
}
