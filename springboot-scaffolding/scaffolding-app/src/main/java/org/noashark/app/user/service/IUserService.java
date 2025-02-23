package org.noashark.app.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.noashark.app.user.pojo.dto.UserDTO;
import org.noashark.app.user.pojo.po.User;
import org.noashark.app.user.pojo.query.UserQuery;
import org.noashark.app.user.pojo.vo.UserVO;
import org.noashark.common.pojo.dto.PageDTO;
import org.noashark.common.pojo.vo.PageVO;

import java.util.List;

public interface IUserService extends IService<User> {

    PageVO<UserVO> voList(PageDTO<UserQuery> pageDTO);

    List<User> findByDeptId(Long deptId);

    boolean save(UserDTO userDTO);

    UserDTO findById(Long id);

    boolean modify(UserDTO userDTO);

    boolean delById(Long id);
}