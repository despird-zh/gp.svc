package com.gp.svc;

import java.util.List;
import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.dao.info.DictionaryInfo;
import com.gp.info.InfoId;

public interface DictionaryService {

	public List<DictionaryInfo> getDictEntries(ServiceContext svcctx, String dictGroup, String keyFilter)throws ServiceException;

	public boolean updateDictEntry(ServiceContext svcctx, DictionaryInfo dictinfo) throws ServiceException;

	public DictionaryInfo getDictEntry(ServiceContext svcctx, InfoId<Long> dictId) throws ServiceException;
	
	public DictionaryInfo getDictEntry(ServiceContext svcctx, String dictKey) throws ServiceException;

	public DictionaryInfo getDictEntry(String dictKey, boolean property) ;
	
	public List<DictionaryInfo> getDictGroupEntries(String dictGroup);
	
}
