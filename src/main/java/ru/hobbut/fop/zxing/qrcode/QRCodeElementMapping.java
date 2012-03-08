/*
 * #%L
 * fop-zxing
 * %%
 * Copyright (C) 2012 Dmitriy Yakovlev
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ru.hobbut.fop.zxing.qrcode;

import org.apache.fop.fo.ElementMapping;
import org.apache.fop.fo.FONode;
import org.w3c.dom.DOMImplementation;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: hobbut
 * Date: 11.02.12
 * Time: 15:23
 */

public class QRCodeElementMapping extends ElementMapping {
    
    public final static String NAMESPACE = "http://hobbut.ru/fop/qr-code/";

    public QRCodeElementMapping() {
        this.namespaceURI = NAMESPACE;
        initialize();
    }

    public DOMImplementation getDOMImplementation() {
        return getDefaultDOMImplementation();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected void initialize() {
        if (foObjs == null) {
            foObjs = new HashMap();
            foObjs.put("qr-code", new QRCodeRootMaker());
            foObjs.put(DEFAULT, new QRCodeMaker());
        }
    }

    static class QRCodeMaker extends ElementMapping.Maker {
        public FONode make(FONode parent) {
            return new QRCodeObject(parent);
        }
    }

    static class QRCodeRootMaker extends ElementMapping.Maker {
        public FONode make(FONode parent) {
            return new QRCodeElement(parent);
        }
    }

}