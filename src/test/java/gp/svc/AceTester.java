package gp.svc;

import com.gp.acl.Ace;
import com.gp.acl.AcePrivilege;
import com.gp.acl.AceType;
import com.gp.common.Cabinets;
import com.gp.util.CommonUtils;

public class AceTester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Ace ace = new Ace(AceType.ANYONE, "", AcePrivilege.BROWSE);
		ace.grantPermission("upgrade","download");
		System.out.println(ace);
		
		System.out.println(ace.getPermissions().toString());
		System.out.println(CommonUtils.toJson(ace.getPermissions()));
		
	}

}
