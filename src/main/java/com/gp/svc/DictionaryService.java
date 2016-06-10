package com.gp.svc;

import java.util.List;
import java.util.Locale;

import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.info.DictionaryInfo;
import com.gp.info.InfoId;

public interface DictionaryService {

	public List<DictionaryInfo> getDictEntries(ServiceContext svcctx)throws ServiceException;

	public List<DictionaryInfo> getDictEntries(ServiceContext svcctx, String dictGroup)throws ServiceException;

	public boolean updateDictEntry(ServiceContext svcctx, DictionaryInfo dictinfo) throws ServiceException;

	public DictionaryInfo getDictEntry(ServiceContext svcctx, InfoId<Long> dictId) throws ServiceException;
	
	public DictionaryInfo getDictEntry(ServiceContext svcctx, String dictKey) throws ServiceException;
	
	public String getMessagePattern(Locale locale, String dictKey);
	
	public String getPropertyName(Locale locale, String dictKey);
	
	public DictionaryInfo getDictEntry(String dictKey) ;
	
	public List<DictionaryInfo> getDictGroupEntries(String dictGroup);
	
}
