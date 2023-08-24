/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.wsdl;

import net.sourceforge.pmd.lang.LanguageRegistry;
import net.sourceforge.pmd.lang.impl.SimpleLanguageModuleBase;
import net.sourceforge.pmd.lang.xml.XmlHandler;

/**
 * Created by bernardo-macedo on 24.06.15.
 */
public class WsdlLanguageModule extends SimpleLanguageModuleBase {
    private static final String ID = "wsdl";

    public WsdlLanguageModule() {
        super(LanguageMetadata.withId(ID).name("WSDL")
                              .extensions("wsdl")
                              .addVersion("1.1")
                              .addDefaultVersion("2.0"),
                new XmlHandler());
    }

    public static WsdlLanguageModule getInstance() {
        return (WsdlLanguageModule) LanguageRegistry.PMD.getLanguageById(ID);
    }
}
