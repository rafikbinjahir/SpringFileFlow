package com.example.rafik.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.rafik.model.FileTransfer;

public interface FileRepository extends MongoRepository<FileTransfer, String> {

}
