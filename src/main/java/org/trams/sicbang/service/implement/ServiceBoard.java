package org.trams.sicbang.service.implement;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trams.sicbang.model.entity.Board;
import org.trams.sicbang.model.enumerate.MessageResponse;
import org.trams.sicbang.model.exception.ApplicationException;
import org.trams.sicbang.model.form.FormBoard;
import org.trams.sicbang.service.BaseService;
import org.trams.sicbang.service.IServiceBoard;

/**
 * Created by voncount on 25/04/2016.
 */
@Service
@Transactional
public class ServiceBoard extends BaseService implements IServiceBoard {

    @Override
    public Board create(FormBoard form) {
        Board board = new Board();
        BeanUtils.copyProperties(form, board);

        return repositoryBoard.save(board);
    }

    @Override
    public Board update(FormBoard form) {
        Board board = repositoryBoard.findOne(form.getSpecification());
        if (board == null) {
            throw new ApplicationException(MessageResponse.EXCEPTION_NOT_FOUND);
        }
        BeanUtils.copyProperties(form, board);
        return repositoryBoard.save(board);
    }

    @Override
    public Page<Board> filter(FormBoard form) {
        return repositoryBoard.findAll(form.getSpecification(), form.getPaging());
    }

    @Override
    public void delete(FormBoard form) {
        Board board = repositoryBoard.findOne(form.getSpecification());
        if (board == null) {
            throw new ApplicationException(MessageResponse.EXCEPTION_NOT_FOUND);
        }
        board.setIsDelete(1);
    }

    @Override
    public Board findOne(FormBoard form) {
        return repositoryBoard.findOne(form.getSpecification());
    }

}
