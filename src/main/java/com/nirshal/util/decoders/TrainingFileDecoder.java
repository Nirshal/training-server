package com.nirshal.util.decoders;

import com.nirshal.model.Training;

import java.io.File;
import java.io.IOException;

public interface TrainingFileDecoder {
    Training decode(File file) throws IOException;
}
