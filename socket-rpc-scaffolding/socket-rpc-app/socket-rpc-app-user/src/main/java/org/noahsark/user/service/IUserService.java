package org.noahsark.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.noahsark.user.common.pojo.dto.PageDTO;
import org.noahsark.user.common.pojo.vo.PageVO;
import org.noahsark.user.pojo.dto.UserDTO;
import org.noahsark.user.pojo.po.User;
import org.noahsark.user.pojo.query.UserQuery;
import org.noahsark.user.pojo.vo.UserVO;

import java.util.List;

public interface IUserService extends IService<User> {

    PageVO<UserVO> voList(PageDTO<UserQuery> pageDTO);

    List<User> findByDeptId(Long deptId);

    boolean save(UserDTO userDTO);

    UserDTO findById(Long id);

    boolean modify(UserDTO userDTO);

    boolean delById(Long id);
}