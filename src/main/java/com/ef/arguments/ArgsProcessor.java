/*
 * Created by Luke DeRienzo on 2/4/19 11:09 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 2/4/19 11:09 PM
 */

package com.ef.arguments;

import com.ef.arguments.extraction.ArgExtractor;
import com.ef.arguments.extraction.ExtractedArgs;
import com.ef.arguments.validation.ArgValidator;
import com.ef.arguments.validation.ValidatedArgs;

public final class ArgsProcessor {
    private ExtractedArgs extractedArgs;

    public ValidatedArgs process(String... args) {
        extractArgsFromCommandLine(args);
        return validateExtractedArgs();
    }

    private void extractArgsFromCommandLine(String... args) {
        extractedArgs = new ArgExtractor().extractFromCommandLine(args);
    }

    private ValidatedArgs validateExtractedArgs() {
        return new ArgValidator().validate(extractedArgs);
    }
}
