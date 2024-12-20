package com.workerservice.model.mapper;

import com.workerservice.model.DTO.AnswerWorkerDTO;
import com.workerservice.model.DTO.WorkerDTO;
import com.workerservice.model.Worker;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WorkerMapper {

    WorkerMapper INSTANCE = Mappers.getMapper(WorkerMapper.class);

    WorkerDTO workerToWorkerDTO(Worker worker);
    Worker workerDTOToWorker(WorkerDTO workerDTO);

    AnswerWorkerDTO workerToAnswerWorkerDTO(Worker worker);
    Worker answerWorkerDTOToWorker(AnswerWorkerDTO answerWorkerDTO);

    AnswerWorkerDTO workerDTOToAnswerWorkerDTO(WorkerDTO workerDTO);
    WorkerDTO workerDTOToAnswerWorkerDTO(AnswerWorkerDTO answerWorkerDTO);
}
