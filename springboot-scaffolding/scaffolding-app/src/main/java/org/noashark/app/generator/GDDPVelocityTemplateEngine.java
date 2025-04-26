package org.noashark.app.generator;

import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.io.File;
import java.util.List;
import java.util.Map;

public class GDDPVelocityTemplateEngine extends VelocityTemplateEngine {

    @Override
    protected void outputCustomFile(List<CustomFile> customFiles, TableInfo tableInfo, Map<String, Object> objectMap) {
        final String parent = this.getPathInfo(OutputFile.parent);
        for (CustomFile customFile : customFiles) {
            String fileName = String.format(customFile.getFileName(), tableInfo.getEntityName());
            this.outputFile(new File(parent + customFile.getFilePath() + File.separator + fileName), objectMap, customFile.getTemplatePath(), customFile.isFileOverride());
        }
    }
}
