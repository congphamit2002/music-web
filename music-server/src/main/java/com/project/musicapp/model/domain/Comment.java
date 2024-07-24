package model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

@TableName(value = "comment")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer songId;

    private Integer songListId;

    private String content;

    private Byte type;

    private Byte up;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
