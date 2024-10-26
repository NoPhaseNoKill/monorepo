/*
 * Copyright 2020 the original author or authors.
 *
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
 */

package com.nophasenokill.docs.dsl.docbook;

import com.nophasenokill.docs.dsl.docbook.model.ClassExtensionDoc;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ExtensionMethodsSummaryRenderer {
    private final MethodTableRenderer methodTableRenderer;

    public ExtensionMethodsSummaryRenderer(MethodTableRenderer methodTableRenderer) {
        this.methodTableRenderer = methodTableRenderer;
    }

    public void renderTo(ClassExtensionDoc extension, Element parent) {
        if (extension.getExtensionMethods().isEmpty()) {
            return;
        }

        Document document = parent.getOwnerDocument();

        Element section = document.createElement("section");
        parent.appendChild(section);

        Element title = document.createElement("title");
        section.appendChild(title);
        title.appendChild(document.createTextNode("Methods added by the "));
        Element literal = document.createElement("literal");
        title.appendChild(literal);
        literal.appendChild(document.createTextNode(extension.getPluginId()));
        title.appendChild(document.createTextNode(" plugin"));

        Element titleabbrev = document.createElement("titleabbrev");
        section.appendChild(titleabbrev);
        literal = document.createElement("literal");
        titleabbrev.appendChild(literal);
        literal.appendChild(document.createTextNode(extension.getPluginId()));
        titleabbrev.appendChild(document.createTextNode(" plugin"));

        Element table = document.createElement("table");
        section.appendChild(table);

        title = document.createElement("title");
        table.appendChild(title);
        title.appendChild(document.createTextNode("Methods - "));
        literal = document.createElement("literal");
        title.appendChild(literal);
        literal.appendChild(document.createTextNode(extension.getPluginId()));
        title.appendChild(document.createTextNode(" plugin"));

        methodTableRenderer.renderTo(extension.getExtensionMethods(), table);
    }
}
