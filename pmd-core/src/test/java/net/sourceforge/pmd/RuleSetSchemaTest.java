/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

class RuleSetSchemaTest {

    private CollectingErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        Locale.setDefault(Locale.ROOT);
        errorHandler = new CollectingErrorHandler();
    }

    @Test
    void verifyVersion2() throws Exception {
        String ruleset = generateRuleSet("2.0.0");
        Document doc = parseWithVersion2(ruleset);
        assertNotNull(doc);

        assertTrue(errorHandler.isValid());

        assertEquals("Custom ruleset", ((Attr) doc.getElementsByTagName("ruleset").item(0).getAttributes().getNamedItem("name")).getValue());
    }

    @Test
    void validateOnly() throws Exception {
        Validator validator = PMDRuleSetEntityResolver.getSchemaVersion2().newValidator();
        validator.setErrorHandler(errorHandler);
        validator.validate(new StreamSource(new ByteArrayInputStream(generateRuleSet("2.0.0").getBytes(StandardCharsets.UTF_8))));
        assertTrue(errorHandler.isValid());
        errorHandler.reset();
    }

    private Document parseWithVersion2(String ruleset) throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setFeature("http://apache.org/xml/features/validation/schema", true);
        DocumentBuilder builder = dbf.newDocumentBuilder();
        builder.setErrorHandler(errorHandler);
        builder.setEntityResolver(new PMDRuleSetEntityResolver());

        return builder.parse(new ByteArrayInputStream(ruleset.getBytes(StandardCharsets.UTF_8)));
    }

    private String generateRuleSet(String version) {
        String versionUnderscore = version.replaceAll("\\.", "_");
        return "<?xml version=\"1.0\"?>\n"
            + "<ruleset \n"
            + "    xmlns=\"http://pmd.sourceforge.net/ruleset/?\"\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:schemaLocation=\"http://pmd.sourceforge.net/ruleset/" + version
            + " https://pmd.sourceforge.io/ruleset_" + versionUnderscore + ".xsd\"\n"
            + "    name=\"Custom ruleset\" >\n"
            + "  <description>\n"
            + "  This ruleset checks my code for bad stuff\n"
            + "  </description>\n"
            + "  <rule name=\"DummyBasicMockRule\" language=\"dummy\" since=\"1.0\" message=\"Test Rule 1\"\n"
            + "        class=\"net.sourceforge.pmd.lang.rule.MockRule\"\n"
            + "        externalInfoUrl=\"${pmd.website.baseurl}/rules/dummy/basic.xml#DummyBasicMockRule\"\n"
            + "  >\n"
            + "        <description>\n"
            + "           Just for test\n"
            + "     </description>\n"
            + "        <priority>3</priority>\n"
            + "        <example>\n"
            + " <![CDATA[\n"
            + " ]]>\n"
            + "     </example>\n"
            + "    </rule>\n"
            + "  <rule ref=\"rulesets/dummy/basic.xml#DummyBasicMockRule\"/>\n"
            + "</ruleset>\n";
    }

    public static class PMDRuleSetEntityResolver implements EntityResolver {
        private static URL schema2 = PMDRuleSetEntityResolver.class.getResource("/ruleset_2_0_0.xsd");
        private static SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        @Override
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            if ("https://pmd.sourceforge.io/ruleset_2_0_0.xsd".equals(systemId)) {
                return new InputSource(schema2.toExternalForm());
            }
            throw new IllegalArgumentException("Unable to resolve entity (publicId=" + publicId + ", systemId=" + systemId + ")");
        }

        public static Schema getSchemaVersion2() throws SAXException {
            return schemaFactory.newSchema(schema2);
        }
    }

    public static class CollectingErrorHandler implements ErrorHandler {
        private List<SAXParseException> warnings = new ArrayList<>();
        private List<SAXParseException> errors = new ArrayList<>();
        private List<SAXParseException> fatalErrors = new ArrayList<>();

        public boolean isValid() {
            return warnings.isEmpty() && errors.isEmpty() && fatalErrors.isEmpty();
        }

        public List<SAXParseException> getWarnings() {
            return warnings;
        }

        public List<SAXParseException> getErrors() {
            return errors;
        }

        public List<SAXParseException> getFatalErrors() {
            return fatalErrors;
        }

        @Override
        public void warning(SAXParseException exception) throws SAXException {
            warnings.add(exception);
        }

        @Override
        public void error(SAXParseException exception) throws SAXException {
            errors.add(exception);
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            fatalErrors.add(exception);
        }

        @Override
        public String toString() {
            return "Warnings: " + warnings + "; Errors: " + errors + "; Fatal Errors: " + fatalErrors;
        }

        public void reset() {
            warnings.clear();
            errors.clear();
            fatalErrors.clear();
        }
    }
}
