package org.jvnet.jaxb2_commons.xjc.model.concrete;

import java.util.HashMap;
import java.util.Map;

import org.jvnet.jaxb2_commons.xml.bind.model.MPackage;
import org.jvnet.jaxb2_commons.xml.bind.model.concrete.CMInfoFactory;
import org.jvnet.jaxb2_commons.xml.bind.model.concrete.CMPackage;

import com.sun.codemodel.JPackage;
import com.sun.tools.xjc.model.CClassInfo;
import com.sun.tools.xjc.model.CClassInfoParent;
import com.sun.tools.xjc.model.CClassInfoParent.Visitor;
import com.sun.tools.xjc.model.CElementInfo;
import com.sun.tools.xjc.model.CEnumLeafInfo;
import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.model.nav.NClass;
import com.sun.tools.xjc.model.nav.NType;
import com.sun.xml.bind.v2.model.core.ClassInfo;
import com.sun.xml.bind.v2.model.core.ElementInfo;
import com.sun.xml.bind.v2.model.core.EnumLeafInfo;

public class XJCCMInfoFactory extends CMInfoFactory<NType, NClass, Void, Void> {

	public XJCCMInfoFactory(Model model) {
		super(model);
	}

	@Override
	protected MPackage getPackage(ClassInfo<NType, NClass> info) {
		final CClassInfo cinfo = (CClassInfo) info;
		return getPackage(cinfo.parent());
	}

	@Override
	protected MPackage getPackage(EnumLeafInfo<NType, NClass> info) {
		return getPackage(((CEnumLeafInfo) info).parent);
	}

	@Override
	protected MPackage getPackage(ElementInfo<NType, NClass> info) {
		return getPackage(((CElementInfo) info).parent);
	}

	private final Map<String, MPackage> packages = new HashMap<String, MPackage>();

	private MPackage getPackage(CClassInfoParent parent) {

		return parent.accept(new Visitor<MPackage>() {

			@Override
			public MPackage onBean(CClassInfo bean) {
				return getPackage(bean.parent());
			}

			@Override
			public MPackage onPackage(JPackage pkg) {
				String packageName = pkg.name();
				MPackage _package = packages.get(packageName);
				if (_package == null) {
					_package = new CMPackage(packageName);
					packages.put(packageName, _package);
				}
				return _package;
			}

			@Override
			public MPackage onElement(CElementInfo element) {
				return getPackage(element.parent);
			}
		});

	}

	@Override
	protected String getLocalName(ClassInfo<NType, NClass> info) {
		return ((CClassInfo) info).shortName;
	}

	@Override
	protected String getLocalName(EnumLeafInfo<NType, NClass> info) {
		return ((CEnumLeafInfo) info).shortName;
	}

}