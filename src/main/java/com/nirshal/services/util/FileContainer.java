package com.nirshal.services.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.ByteArrayInputStream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileContainer {

    private String fileName;
    private ByteArrayInputStream file;

}
