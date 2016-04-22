package gp.svc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.gp.acl.AcePrivilege;
import com.gp.acl.AceType;
import com.gp.common.GeneralConstants;
import com.gp.common.IdKey;
import com.gp.common.Principal;
import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.info.CabAceInfo;
import com.gp.info.CabEntryInfo;
import com.gp.info.CabFileInfo;
import com.gp.info.CabFolderInfo;
import com.gp.info.InfoId;
import com.gp.info.UserInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.svc.AclService;
import com.gp.svc.CabinetService;
import com.gp.svc.FolderService;
import com.gp.svc.IdService;
import com.gp.svc.SecurityService;

@ContextConfiguration(locations = "/mysql-test.xml")
public class CabFolderServiceTest extends AbstractJUnit4SpringContextTests{
	
	Principal principal = new Principal("demouser");
	ServiceContext<?> svcctx ;
	
	@Autowired
    private IdService idService;
	
	@Autowired
    private FolderService folderService;
	
	@Autowired
    private CabinetService cabinetService;
	
	@Autowired
    private AclService aclService;
	
	public void test() throws Exception{
		
		svcctx = new ServiceContext(principal);		
		CabFolderInfo cfolderinfo = new CabFolderInfo();
		InfoId<Long> folderkey = idService.generateId(IdKey.CAB_FOLDER, Long.class);
		
		cfolderinfo.setInfoId(folderkey);
		cfolderinfo.setEntryName("demo folder");
		cfolderinfo.setTotalSize(5643l);
		cfolderinfo.setHashCode("ss");
		cfolderinfo.setOwm(234l);
		cfolderinfo.setState("ready");
		InfoId<Long> aid = newAcl();
		cfolderinfo.setAclId(aid.getId());
		InfoId<Long> pkey = IdKey.USER.getInfoId(46l);
		cfolderinfo.setCabinetId(3l);
		folderService.newFolder(svcctx, pkey, cfolderinfo);
		
	}


	public void test_file_search() throws Exception{
		
		svcctx = new ServiceContext(principal);		
		InfoId<Long> ckey = IdKey.CABINET.getInfoId(123l);
		InfoId<Long> folderkey = IdKey.CAB_FOLDER.getInfoId(12l);
		List<CabFileInfo> flist = cabinetService.getCabFiles(svcctx, ckey, folderkey);
		for(CabFileInfo cfi: flist){
			System.out.println("1 -- "+cfi.getEntryName());
		}
	}

	public void test_file_pagequery() throws Exception{
		
		svcctx = new ServiceContext(principal);		
		InfoId<Long> ckey = IdKey.CABINET.getInfoId(123l);
		InfoId<Long> folderkey = IdKey.CAB_FOLDER.getInfoId(12l);
		PageQuery pquery = new PageQuery(10,1);
		
		PageWrapper<CabFileInfo> pwrapper = cabinetService.getCabFiles(svcctx, ckey, folderkey,"f", pquery);
		for(CabFileInfo cfi: pwrapper.getRows()){
			System.out.println("2 -- "+cfi.getEntryName());
		}
	}
	
	public void test_folder_search() throws Exception{
		
		svcctx = new ServiceContext(principal);		
		InfoId<Long> ckey = IdKey.CABINET.getInfoId(123l);
		InfoId<Long> folderkey = IdKey.CAB_FOLDER.getInfoId(12l);
		List<CabFolderInfo> flist = cabinetService.getCabFolders(svcctx, ckey, folderkey);
		for(CabFolderInfo cfi: flist){
			System.out.println("1 -- "+cfi.getEntryName());
		}
	}
	
	public void test_folder_pagequery() throws Exception{
		
		svcctx = new ServiceContext(principal);		
		InfoId<Long> ckey = IdKey.CABINET.getInfoId(123l);
		InfoId<Long> folderkey = IdKey.CAB_FOLDER.getInfoId( 12l);
		PageQuery pquery = new PageQuery(10,1);
		
		PageWrapper<CabFolderInfo> pwrapper = cabinetService.getCabFolders(svcctx, ckey, folderkey,"foler1", pquery);
		for(CabFolderInfo cfi: pwrapper.getRows()){
			System.out.println("2 -- "+cfi.getEntryName());
		}
	}
	
	@Test
	public void test_entry_pagequery() throws Exception{
		
		svcctx = new ServiceContext(principal);		
		InfoId<Long> ckey = IdKey.CABINET.getInfoId( 123l);
		InfoId<Long> folderkey = IdKey.CAB_FOLDER.getInfoId( 12l);
		PageQuery pquery = new PageQuery(10,1);
		
		PageWrapper<CabEntryInfo> pwrapper = cabinetService.getCabEntries(svcctx, ckey, folderkey,"foler1", pquery);
		for(CabEntryInfo cfi: pwrapper.getRows()){
			System.out.println("2 -- "+cfi.getEntryName());
		}
	}
	
	public InfoId<Long> newAcl() throws ServiceException{
		List<CabAceInfo> acelist = new ArrayList<CabAceInfo>();

		CabAceInfo aceowner = new CabAceInfo();
		aceowner.setSubjectType(AceType.OWNER.value);
		aceowner.setSubject(GeneralConstants.OWNER_SUBJECT);
		aceowner.setPrivilege(AcePrivilege.DELETE.value | AcePrivilege.EXEC.value);
		aceowner.setPermissions("[\"print\",\"browse\",\"copy\"]");
		acelist.add(aceowner);
		
		CabAceInfo aceowner1 = new CabAceInfo();
		aceowner1.setSubjectType(AceType.GROUP.value);
		aceowner1.setSubject("demogrp");
		aceowner1.setPrivilege(AcePrivilege.DELETE.value | AcePrivilege.EXEC.value);
		aceowner1.setPermissions("[\"print\",\"browse\",\"copy\"]");
		acelist.add(aceowner1);
		
		aceowner1 = new CabAceInfo();
		aceowner1.setSubjectType(AceType.USER.value);
		aceowner1.setSubject("demousr");
		aceowner1.setPrivilege(AcePrivilege.DELETE.value | AcePrivilege.EXEC.value);
		aceowner1.setPermissions("[\"print\",\"browse\",\"copy\"]");
		acelist.add(aceowner1);
		
		return aclService.addAclInfo(svcctx, acelist);
	}
	
	
}
