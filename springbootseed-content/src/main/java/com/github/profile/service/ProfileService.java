package com.github.profile.service;

import com.github.profile.domain.Profile;
import com.github.profile.service.dto.ProfileDTO;
import com.github.profile.service.dto.ProfileQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author oldhand
* @date 2019-12-27
*/
public interface ProfileService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ProfileQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ProfileDTO>
    */
    List<ProfileDTO> queryAll(ProfileQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ProfileDTO
     */
    ProfileDTO findById(String id);

    ProfileDTO create(Profile resources);

    void update(Profile resources);

    void delete(String id);

    void download(List<ProfileDTO> all, HttpServletResponse response) throws IOException;
}