package com.telechurn.ai.ml;

import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ModelManager {

    private final File modelDirectory;

    public ModelManager(@Value("${model.directory:models}") String modelDirectoryPath) {
        this.modelDirectory = new File(modelDirectoryPath);
        if (!this.modelDirectory.exists()) {
            this.modelDirectory.mkdirs();
        }
    }

    public File decisionTreeFile() {
        return new File(modelDirectory, "decision-tree.model");
    }

    public File kmeansFile() {
        return new File(modelDirectory, "kmeans.model");
    }

    public void ensureDirectory() throws IOException {
        if (!modelDirectory.exists() && !modelDirectory.mkdirs()) {
            throw new IOException("Could not create model directory");
        }
    }
}
