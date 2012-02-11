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

import org.apache.fop.fo.FONode;
import org.apache.fop.fo.XMLObj;

/**
 * Created with IntelliJ IDEA.
 * User: hobbut
 * Date: 11.02.12
 * Time: 15:25
 */
public class QRCodeObject extends XMLObj {

    public QRCodeObject(FONode parent) {
        super(parent);
    }

    @Override
    public String getNamespaceURI() {
        return QRCodeElementMapping.NAMESPACE;
    }

    @Override
    public String getNormalNamespacePrefix() {
        return "qr";
    }
}