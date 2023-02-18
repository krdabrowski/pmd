/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Collects the result of a command execution in order to verify it.
 *
 * @author Andreas Dangel
 */
public class ExecutionResult {
    private final int exitCode;
    private final String output;
    private final String errorOutput;
    private final String report;

    ExecutionResult(int theExitCode, String theOutput, String theErrorOutput, String theReport) {
        this.exitCode = theExitCode;
        this.output = theOutput;
        this.errorOutput = theErrorOutput;
        this.report = theReport;
    }

    @Override
    public String toString() {
        return "ExecutionResult:\n"
            + " exit code: " + exitCode + "\n"
            + " output:\n" + output + "\n"
            + " errorOutput:\n" + errorOutput + "\n"
            + " report:\n" + report + "\n";
    }

    /**
     * Asserts that the command exited with the expected exit code. Any output is ignored.
     *
     * @param expectedExitCode the exit code, e.g. 0 if no rule violations are expected, or 4 if violations are found
     */
    public void assertExecutionResult(int expectedExitCode) {
        assertExecutionResult(expectedExitCode, null);
    }

    /**
     * Asserts that the command exited with the expected exit code and that the given expected
     * output is contained in the actual command output.
     *
     * @param expectedExitCode the exit code, e.g. 0 if no rule violations are expected, or 4 if violations are found
     * @param expectedOutput the output to search for
     */
    public void assertExecutionResult(int expectedExitCode, String expectedOutput) {
        assertExecutionResult(expectedExitCode, expectedOutput, null);
    }

    /**
     * Asserts that the command exited with the expected exit code and that the given expected
     * output is contained in the actual command output and the given expected report is in the
     * generated report.
     *
     * @param expectedExitCode the exit code, e.g. 0 if no rule violations are expected, or 4 if violations are found
     * @param expectedOutput   the output to search for
     * @param expectedReport   the string to search for tin the report
     */
    public void assertExecutionResult(int expectedExitCode, String expectedOutput, String expectedReport) {
        assertExecResultImpl(expectedExitCode, output, expectedOutput, expectedReport);
    }

    /**
     * Asserts that the command exited with the expected exit code and that the given expected
     * output is contained in the actual command ERROR output, and the given expected report is in the
     * generated report.
     *
     * @param expectedExitCode    the exit code, e.g. 0 if no rule violations are expected, or 4 if violations are found
     * @param expectedErrorOutput the output to search for in stderr
     * @param expectedReport      the string to search for tin the report
     */
    public void assertExecutionResultErrOutput(int expectedExitCode, String expectedErrorOutput, String expectedReport) {
        assertExecResultImpl(expectedExitCode, errorOutput, expectedErrorOutput, expectedReport);
    }

    /**
     * Asserts that the command exited with the expected exit code and that the given expected
     * output is contained in the actual command ERROR output.
     *
     * @param expectedExitCode    the exit code, e.g. 0 if no rule violations are expected, or 4 if violations are found
     * @param expectedErrorOutput the output to search for in stderr
     */
    public void assertExecutionResultErrOutput(int expectedExitCode, String expectedErrorOutput) {
        assertExecResultImpl(expectedExitCode, errorOutput, expectedErrorOutput, null);
    }

    private void assertExecResultImpl(int expectedExitCode, String output, String expectedOutput, String expectedReport) {
        assertEquals(expectedExitCode, exitCode, "Command exited with wrong code.\nComplete result:\n\n" + this);
        assertNotNull(output, "No output found");
        if (expectedOutput != null && !expectedOutput.isEmpty()) {
            if (!output.contains(expectedOutput)) {
                fail("Expected output '" + expectedOutput + "' not present.\nComplete result:\n\n" + this);
            }
        } else if (expectedOutput != null && expectedOutput.isEmpty()) {
            assertTrue(output.isEmpty(), "The output should have been empty.\nComplete result:\n\n" + this);
        }
        if (expectedReport != null && !expectedReport.isEmpty()) {
            assertTrue(report.contains(expectedReport),
                    "Expected report '" + expectedReport + "'.\nComplete result:\n\n" + this);
        }
    }

    /**
     * Asserts that the given error message is not in the error output.
     * @param errorMessage the error message to search for
     */
    public void assertNoError(String errorMessage) {
        assertFalse(errorOutput.contains(errorMessage),
                "Found error message: " + errorMessage + ".\nComplete result:\n\n" + this);
    }

    /**
     * Asserts that the given error message is not in the report.
     * @param errorMessage the error message to search for
     */
    public void assertNoErrorInReport(String errorMessage) {
        assertFalse(report.contains(errorMessage),
                "Found error message in report: " + errorMessage + ".\nComplete result:\n\n" + this);
    }

    public void assertErrorOutputContains(String message) {
        assertTrue(errorOutput.contains(message), "erroroutput didn't contain " + message);
    }

    static class Builder {
        private int exitCode;
        private String output;
        private String errorOutput;
        private String report;

        Builder withExitCode(int exitCode) {
            this.exitCode = exitCode;
            return this;
        }

        Builder withOutput(String output) {
            this.output = output;
            return this;
        }

        Builder withErrorOutput(String errorOutput) {
            this.errorOutput = errorOutput;
            return this;
        }

        Builder withReport(String report) {
            this.report = report;
            return this;
        }

        ExecutionResult build() {
            return new ExecutionResult(exitCode, output, errorOutput, report);
        }
    }
}
