package com.nirshal.model.mappers;

import com.nirshal.model.Training;
import com.nirshal.model.TrainingInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface TrainingMapper {

    TrainingInfo trainingToTrainingInfo(Training training);
}
