package ${package.Mapper};

import ${package.Entity}.${entity};
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


@Mapper
public interface ${table.mapperName} extends BaseMapper<${entity}> {

}

