package com.gp.svc;

import com.gp.exception.ServiceException;
import com.gp.info.Identifier;
import com.gp.info.InfoId;

public interface IdService {

	public <T> InfoId<T> generateId( Identifier idkey,Class<T> keytype) throws ServiceException;

	public <T> InfoId<T> generateId(String modifier, Identifier idkey, Class<T> type) throws ServiceException;

}
