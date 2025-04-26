package org.noashark.app.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.builder.Entity;
import org.apache.ibatis.annotations.Mapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CodeGenerator {

    public static void main(String[] args) {
        AutoGenerator generator = new AutoGenerator(DATA_SOURCE_CONFIG);
        generator.strategy(strategyConfig().build());
        generator.global(globalConfig().build());
        generator.packageInfo(packageConfig().build());
        generator.template(templateConfig().build());
        generator.injection(injectionConfig().build());
        generator.execute(new GDDPVelocityTemplateEngine());
    }

    public static TemplateConfig.Builder templateConfig() {
        return new TemplateConfig.Builder()
                .controller("templates/controller.java")
                .service("templates/service.java")
                .serviceImpl("templates/serviceImpl.java")
                .mapper("templates/mapper.java")
                .entity("templates/po.java");
    }

    /**
     * 数据源配置
     */
    private static final DataSourceConfig DATA_SOURCE_CONFIG = new DataSourceConfig
            .Builder("jdbc:postgresql://127.0.0.1:5432/scaffolding?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&allowMultiQueries=true", "postgres", "postgres")
            .schema("public")
            .build();

    private static List<String> getTableNames() {
        List<String> list = new ArrayList<>();
        list.add("t_user");
        return list;
    }

    private static String getOutputDir() {
        String dir = CodeGenerator.class.getResource("/").getPath().replace("target/classes", "src/main/java");
        return dir.replaceFirst("/", "");
        // return "D:\\02_workspace\\04_project\\gddp\\src\\main\\java";
    }

    /**
     * 策略配置
     */
    protected static StrategyConfig.Builder strategyConfig() {
        StrategyConfig.Builder builder = new StrategyConfig.Builder();
        Entity.Builder entityBuilder = builder.entityBuilder();
        entityBuilder.enableLombok().enableFileOverride().idType(IdType.ASSIGN_ID);
        builder.controllerBuilder().enableRestStyle().enableFileOverride();
        builder.serviceBuilder().enableFileOverride();
        builder.mapperBuilder().enableFileOverride().mapperAnnotation(Mapper.class);
        builder.addInclude(getTableNames())
                .addTablePrefix("t_");
        return builder;
    }

    /**
     * 全局配置
     */
    protected static GlobalConfig.Builder globalConfig() {
        return new GlobalConfig.Builder()
                .author("allen")
                .outputDir(getOutputDir());
    }

    /**
     * 包配置
     */
    protected static PackageConfig.Builder packageConfig() {
        return new PackageConfig.Builder().parent("org.noashark.app")
                .entity("pojo.po")
                .moduleName("user1");
    }

    protected static InjectionConfig.Builder injectionConfig() {
        CustomFile dto = new CustomFile.Builder()
                .fileName("%sDTO.java")
                .filePath(File.separator + "pojo" + File.separator + "dto" + File.separator)
                .templatePath("templates/dto.java.vm")
                .packageName("pojo.dto")
                .enableFileOverride().build();
        CustomFile vo = new CustomFile.Builder()
                .fileName("%sVO.java")
                .filePath(File.separator + "pojo" + File.separator + "vo" + File.separator)
                .templatePath("templates/vo.java.vm")
                .packageName("pojo.vo")
                .enableFileOverride().build();
        /*CustomFile po = new CustomFile.Builder()
                .fileName("%s.java")
                .filePath(File.separator + "pojo" + File.separator + "po" + File.separator)
                .templatePath("templates/po.java.vm")
                .packageName("pojo.po")
                .enableFileOverride().build();*/
        CustomFile mapstruct = new CustomFile.Builder()
                .fileName("%sMapstruct.java")
                .filePath(File.separator + "pojo" + File.separator + "mapstruct" + File.separator)
                .templatePath("templates/mapstruct.java.vm")
                .packageName("pojo.mapstruct")
                .enableFileOverride().build();
        CustomFile query = new CustomFile.Builder()
                .fileName("%sQuery.java")
                .filePath(File.separator + "pojo" + File.separator + "query" + File.separator)
                .templatePath("templates/query.java.vm")
                .packageName("pojo.query")
                .enableFileOverride().build();

        List<CustomFile> customFiles = new ArrayList<>();
        customFiles.add(dto);
        customFiles.add(vo);
        /*customFiles.add(po);*/
        customFiles.add(mapstruct);
        customFiles.add(query);
        InjectionConfig.Builder builder = new InjectionConfig.Builder();
        builder.customFile(customFiles);
        return builder;
    }
}
