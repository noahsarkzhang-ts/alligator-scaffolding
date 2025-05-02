package org.noahsark.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.noahsark.user.common.pojo.dto.PageDTO;
import org.noahsark.user.common.pojo.vo.PageVO;
import org.noahsark.user.mapper.UserMapper;
import org.noahsark.user.pojo.dto.UserDTO;
import org.noahsark.user.pojo.mapstruct.UserMapstruct;
import org.noahsark.user.pojo.po.User;
import org.noahsark.user.pojo.query.UserQuery;
import org.noahsark.user.pojo.vo.UserVO;
import org.noahsark.user.service.IUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 * @author zhangxt
 * @date 2025/02/23 19:26
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public PageVO<UserVO> voList(PageDTO<UserQuery> pageDTO) {

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        UserQuery query = pageDTO.getQuery();
        if (!Objects.isNull(query)) {
            wrapper.like(StringUtils.isNotEmpty(query.getNickname()), User::getNickname, query.getNickname());
            wrapper.eq(!Objects.isNull(query.getDeptId()), User::getDeptId, query.getDeptId());
        }
        IPage<User> page = baseMapper.selectPage(Page.of(pageDTO.getCurPage(), pageDTO.getPageSize()), wrapper);
        PageVO<UserVO> pageVO = new PageVO<>();
        pageVO.setCurrent(pageDTO.getCurPage());
        pageVO.setRecords(toVoList(page.getRecords()));
        pageVO.setTotalRows(page.getTotal());


        return pageVO;

    }

    @Override
    public List<User> findByDeptId(Long deptId) {
        return null;
    }

    @Override
    public boolean save(UserDTO userDTO) {

        User userPO = UserMapstruct.INSTANCE.toPO(userDTO);

        userPO.setId(IdWorker.getId(userPO));
        userPO.setPassword("123456");
        userPO.setOperatedBy(1L);
        userPO.setCreatedAt(LocalDateTime.now());
        userPO.setUpdatedAt(LocalDateTime.now());
        userPO.setTenantId(1L);
        userPO.setCustomerId(1L);

        return this.save(userPO);
    }

    @Override
    public UserDTO findById(Long id) {

        User userPo = this.getById(id);
        if (userPo == null) {
            return null;
        }

        UserDTO userDTO = UserMapstruct.INSTANCE.toDTO(userPo);

        return userDTO;
    }

    @Override
    public boolean modify(UserDTO user) {
        User userPO = UserMapstruct.INSTANCE.toPO(user);
        userPO.setUpdatedAt(LocalDateTime.now());
        userPO.setOperatedBy(1L);
        if (StringUtils.isNotEmpty(userPO.getPassword())) {
            userPO.setPassword("123456");
        }

        return this.updateById(userPO);
    }

    @Override
    public boolean delById(Long id) {
        return this.removeById(id);
    }

    private List<UserVO> toVoList(List<User> poList) {
        return poList.stream().map(user -> UserMapstruct.INSTANCE.toVO(user)).collect(Collectors.toList());
    }
}
