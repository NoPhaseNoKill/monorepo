package com.nophasenokill.client.ui.connected.actions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.times
import com.nophasenokill.client.core.gradle.dcl.*
import com.nophasenokill.client.ui.composables.*
import com.nophasenokill.client.ui.theme.spacing
import com.nophasenokill.client.ui.theme.transparency
import org.gradle.declarative.dsl.schema.DataClass
import org.gradle.declarative.dsl.schema.DataProperty
import org.gradle.declarative.dsl.schema.SchemaMemberFunction
import org.gradle.internal.declarativedsl.dom.DeclarativeDocument
import org.gradle.internal.declarativedsl.dom.DocumentResolution.ElementResolution.SuccessfulElementResolution.ContainerElementResolved
import org.gradle.internal.declarativedsl.dom.DocumentResolution.PropertyResolution.PropertyAssignmentResolved
import org.gradle.internal.declarativedsl.dom.data.NodeData
import org.gradle.internal.declarativedsl.dom.mutation.*
import org.gradle.internal.declarativedsl.dom.operations.overlay.OverlayNodeOrigin.*
import org.gradle.internal.declarativedsl.dom.operations.overlay.OverlayOriginContainer
import org.gradle.internal.declarativedsl.dom.resolution.DocumentResolutionContainer
import org.jetbrains.skiko.Cursor

private const val NOT_DECLARED = "Not declared"
private const val NOTHING_DECLARED = "Nothing declared"

private
class ModelTreeRendering(
    val resolutionContainer: DocumentResolutionContainer,
    val highlightingContext: HighlightingContext,
    val mutationApplicability: NodeData<List<ApplicableMutation>>,
    val onRunMutation: (MutationDefinition, MutationArgumentContainer) -> Unit
) {
    private val indentDp = MaterialTheme.spacing.level2

    @Composable
    fun ModelTreeRendering.ElementInfoOrNothingDeclared(
        type: DataClass?,
        node: DeclarativeDocument.DocumentNode.ElementNode?,
        indentLevel: Int,
    ) {
        Column(Modifier.padding(start = indentLevel * indentDp)) {
            if (node == null || type == null) {
                LabelMedium(modifier = Modifier.alpha(MaterialTheme.transparency.HALF), text = NOT_DECLARED)
            } else {
                type.properties.forEach { property ->
                    PropertyInfo(node.property(property.name), property)
                }
                val accessAndConfigure = type.memberFunctions.accessAndConfigure
                val accessAndConfigureNames = accessAndConfigure.map { it.simpleName }
                accessAndConfigure.forEach { subFunction ->
                    ConfiguringFunctionInfo(subFunction, node, indentLevel)
                    MaterialTheme.spacing.VerticalLevel2()
                }
                val addAndConfigure = type.memberFunctions.addAndConfigure.filter { function ->
                    function.simpleName !in accessAndConfigureNames
                }
                val addAndConfigureByName = addAndConfigure.associateBy { it.simpleName }
                val elementsByAddAndConfigure = node.content
                    .filterIsInstance<DeclarativeDocument.DocumentNode.ElementNode>()
                    .filter { it.name in addAndConfigureByName }

                elementsByAddAndConfigure.forEach { element ->
                    AddingFunctionInfo(element, indentLevel)
                }
            }
        }
    }

    @Composable
    private fun ModelTreeRendering.AddingFunctionInfo(
        element: DeclarativeDocument.DocumentNode.ElementNode,
        indentLevel: Int
    ) {
        val arguments = when (val valueNode = element.elementValues.singleOrNull()) {
            null -> ""
            is DeclarativeDocument.ValueNode.LiteralValueNode -> valueNode.value
            is DeclarativeDocument.ValueNode.ValueFactoryNode -> {
                val args = valueNode.values.map {
                    (it as? DeclarativeDocument.ValueNode.LiteralValueNode)?.value ?: "..."
                }
                val argsString = args.joinToString(",", "(", ")") ?: "()"
                "${valueNode.factoryName}$argsString"
            }
        }
        val elementType =
            (resolutionContainer.data(element) as? ContainerElementResolved)?.elementType as? DataClass

        val elementTextRepresentation = "${element.name}($arguments)"

        val isEmpty = elementType == null || element.content.isEmpty()
        WithDecoration(element) {
            if (isEmpty) {
                LabelMedium(
                    modifier = Modifier.padding(bottom = MaterialTheme.spacing.level2)
                        .withHoverCursor()
                        .withClickTextRangeSelection(element, highlightingContext)
                        .semiTransparentIfNull(element),
                    text = elementTextRepresentation
                )
            } else {
                TitleSmall(
                    text = elementTextRepresentation,
                    modifier = Modifier
                        .withHoverCursor()
                        .withClickTextRangeSelection(element, highlightingContext)
                        .semiTransparentIfNull(element)
                )
            }
        }
        if (!isEmpty) {
            ElementInfoOrNothingDeclared(
                elementType,
                element,
                indentLevel + 1
            )
        }
    }

    @Composable
    private fun ModelTreeRendering.ConfiguringFunctionInfo(
        subFunction: SchemaMemberFunction,
        parentNode: DeclarativeDocument.DocumentNode.ElementNode,
        indentLevel: Int
    ) {
        val functionNode = parentNode.childElementNode(subFunction.simpleName)
        val functionType = functionNode?.type(resolutionContainer) as? DataClass
        WithDecoration(functionNode) {
            TitleSmall(
                text = subFunction.simpleName,
                modifier = Modifier
                    .withHoverCursor()
                    .semiTransparentIfNull(functionType)
                    .withClickTextRangeSelection(functionNode, highlightingContext)
            )
        }
        ElementInfoOrNothingDeclared(
            functionType,
            functionNode,
            indentLevel + 1
        )
    }


    @Composable
    private fun PropertyInfo(
        propertyNode: DeclarativeDocument.DocumentNode.PropertyNode?,
        property: DataProperty
    ) {
        WithDecoration(propertyNode) {
            val maybeInvalidDecoration =
                if (propertyNode != null && resolutionContainer.data(propertyNode) !is PropertyAssignmentResolved)
                    TextDecoration.LineThrough else TextDecoration.None
            LabelMedium(
                modifier = Modifier.padding(bottom = MaterialTheme.spacing.level2)
                    .withHoverCursor()
                    .withClickTextRangeSelection(propertyNode, highlightingContext)
                    .semiTransparentIfNull(propertyNode),
                textStyle = TextStyle(textDecoration = maybeInvalidDecoration),
                text = "${property.name}: ${property.kotlinType.simpleName} = ${
                    propertyNode?.value?.sourceData?.text() ?: NOTHING_DECLARED
                }"
            )
        }
    }

    @Composable
    fun WithDecoration(
        node: DeclarativeDocument.DocumentNode?,
        content: @Composable () -> Unit
    ) {
        WithApplicableMutations(node) {
            WithModelHighlighting(node) {
                content()
            }
        }
    }

    @Composable
    private fun WithModelHighlighting(
        propertyNode: DeclarativeDocument.DocumentNode?,
        content: @Composable () -> Unit,
    ) {
        val sourceData = propertyNode?.sourceData
        val isHighlighted = sourceData?.indexRange?.let { propRange ->
            highlightingContext.highlightedSourceRange.value[sourceData.sourceIdentifier.fileIdentifier]
                ?.let { highlightedRange ->
                    propRange.first >= highlightedRange.first && propRange.last <= highlightedRange.last
                }
        }
        if (isHighlighted == true) {
            Surface(
                shape = MaterialTheme.shapes.extraSmall,
                color = Color.Yellow,
            ) {
                content()
            }
        } else {
            content()
        }
    }

    @Composable
    fun WithApplicableMutations(
        element: DeclarativeDocument.DocumentNode?,
        content: @Composable () -> Unit
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.level1),
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()

            if (element != null) {
                ApplicableMutations(element)
            }
        }
    }

    @Composable
    @Suppress("LongMethod", "CyclomaticComplexMethod")
    private fun ApplicableMutations(node: DeclarativeDocument.DocumentNode) {
        val applicableMutations = mutationApplicability.data(node)
        if (applicableMutations.isNotEmpty()) {
            var isMutationMenuVisible by remember { mutableStateOf(false) }
            val tooltip = "Applicable mutations"
            PlainTextTooltip(tooltip) {
                IconButton(
                    modifier = Modifier
                        .padding(MaterialTheme.spacing.level0)
                        .sizeIn(maxWidth = MaterialTheme.spacing.level6, maxHeight = MaterialTheme.spacing.level6),
                    onClick = { isMutationMenuVisible = true }
                ) {
                    Icon(
                        Icons.Default.Edit,
                        modifier = Modifier.size(MaterialTheme.spacing.level6),
                        contentDescription = tooltip
                    )
                }
            }
            DropdownMenu(
                expanded = isMutationMenuVisible,
                onDismissRequest = { isMutationMenuVisible = false },
            ) {
                var selectedMutation by remember { mutableStateOf<ApplicableMutation?>(null) }
                when (val mutation = selectedMutation) {
                    null -> {
                        MutationDropDownTitle(tooltip)
                        applicableMutations.forEach { applicableMutation ->
                            ListItem(
                                leadingContent = {
                                    Icon(
                                        Icons.Default.ChevronRight,
                                        applicableMutation.mutationDefinition.name
                                    )
                                },
                                headlineContent = { Text(applicableMutation.mutationDefinition.name) },
                                supportingContent = { Text(applicableMutation.mutationDefinition.description) },
                                modifier = Modifier.selectable(selected = false, onClick = {
                                    selectedMutation = applicableMutation
                                }),
                            )
                        }
                    }

                    else -> {
                        var mutationArguments: List<MutationArgumentState> by remember {
                            mutableStateOf(mutation.mutationDefinition.parameters.map { parameter ->
                                when (parameter.kind) {
                                    MutationParameterKind.BooleanParameter ->
                                        MutationArgumentState.BooleanArgument(parameter)

                                    MutationParameterKind.IntParameter ->
                                        MutationArgumentState.IntArgument(parameter)

                                    MutationParameterKind.StringParameter ->
                                        MutationArgumentState.StringArgument(parameter)
                                }
                            })
                        }
                        val validArguments by derivedStateOf {
                            mutationArguments.all { argument ->
                                when (argument) {
                                    is MutationArgumentState.BooleanArgument -> argument.value != null
                                    is MutationArgumentState.IntArgument -> argument.value != null
                                    is MutationArgumentState.StringArgument -> argument.value?.isNotBlank() == true
                                }
                            }
                        }
                        MutationDropDownTitle(
                            headline = mutation.mutationDefinition.name,
                            supporting = mutation.mutationDefinition.description
                        )
                        mutationArguments.forEachIndexed { index, argument ->
                            when (argument) {
                                is MutationArgumentState.BooleanArgument ->
                                    ListItem(
                                        headlineContent = { Text(argument.parameter.name) },
                                        supportingContent = { Text(argument.parameter.description) },
                                        trailingContent = {
                                            Checkbox(
                                                checked = argument.value ?: false,
                                                onCheckedChange = { newChecked ->
                                                    mutationArguments = mutationArguments.toMutableList().apply {
                                                        this[index] = argument.copy(value = newChecked)
                                                    }
                                                }
                                            )
                                        }
                                    )

                                is MutationArgumentState.IntArgument ->
                                    ListItem(headlineContent = {
                                        OutlinedTextField(
                                            modifier = Modifier.fillMaxWidth(),
                                            label = { Text(argument.parameter.name) },
                                            placeholder = { Text(argument.parameter.description) },
                                            value = argument.value?.toString() ?: "",
                                            onValueChange = { newValue ->
                                                mutationArguments = mutationArguments.toMutableList().apply {
                                                    this[index] = argument.copy(value = newValue.toIntOrNull())
                                                }
                                            },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        )
                                    })

                                is MutationArgumentState.StringArgument ->
                                    ListItem(headlineContent = {
                                        OutlinedTextField(
                                            modifier = Modifier.fillMaxWidth(),
                                            label = { Text(argument.parameter.name) },
                                            placeholder = { Text(argument.parameter.description) },
                                            value = argument.value ?: "",
                                            onValueChange = { newValue ->
                                                mutationArguments = mutationArguments.toMutableList().apply {
                                                    this[index] = argument.copy(value = newValue)
                                                }
                                            }
                                        )
                                    })
                            }
                        }
                        ListItem(headlineContent = {}, trailingContent = {
                            Button(
                                content = {
                                    val text = "Apply mutation"
                                    Icon(Icons.Default.Edit, text)
                                    MaterialTheme.spacing.HorizontalLevel2()
                                    Text(text)
                                },
                                enabled = validArguments,
                                onClick = {
                                    onRunMutation(
                                        mutation.mutationDefinition,
                                        mutationArguments.toMutationArgumentsContainer()
                                    )
                                    isMutationMenuVisible = false
                                },
                            )
                        })
                    }
                }
            }
        }
    }
}

@Composable
private fun MutationDropDownTitle(
    headline: String,
    supporting: String? = null
) {
    ListItem(
        headlineContent = { TitleMedium(headline) },
        supportingContent = supporting?.let { { Text(supporting) } },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            headlineColor = MaterialTheme.colorScheme.onSecondaryContainer,
            supportingColor = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    )

}

private sealed interface MutationArgumentState {

    val parameter: MutationParameter<*>

    data class IntArgument(
        override val parameter: MutationParameter<*>,
        val value: Int? = null,
    ) : MutationArgumentState

    data class StringArgument(
        override val parameter: MutationParameter<*>,
        val value: String? = null
    ) : MutationArgumentState

    data class BooleanArgument(
        override val parameter: MutationParameter<*>,
        val value: Boolean? = null
    ) : MutationArgumentState
}

@Suppress("UNCHECKED_CAST")
private fun List<MutationArgumentState>.toMutationArgumentsContainer(): MutationArgumentContainer =
    mutationArguments {
        forEach { argumentState ->
            when (argumentState) {
                is MutationArgumentState.IntArgument ->
                    argument(argumentState.parameter as MutationParameter<Int>, requireNotNull(argumentState.value))

                is MutationArgumentState.StringArgument ->
                    argument(
                        argumentState.parameter as MutationParameter<String>,
                        requireNotNull(argumentState.value)
                    )

                is MutationArgumentState.BooleanArgument ->
                    argument(
                        argumentState.parameter as MutationParameter<Boolean>,
                        requireNotNull(argumentState.value)
                    )
            }
        }
    }


private data class HighlightingContext(
    val overlayOriginContainer: OverlayOriginContainer,
    val highlightedSourceRange: MutableState<Map<String, IntRange>>
)

private fun Modifier.withHoverCursor() =
    pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)))

@OptIn(ExperimentalFoundationApi::class)
private fun Modifier.withClickTextRangeSelection(
    node: DeclarativeDocument.DocumentNode?,
    highlightingContext: HighlightingContext
) = onClick {
    fun DeclarativeDocument.DocumentNode.sourceIdentifierToRange() =
        sourceData.sourceIdentifier.fileIdentifier to sourceData.indexRange

    highlightingContext.highlightedSourceRange.value = if (node == null) {
        emptyMap()
    } else {
        val ownIdToRange = node.sourceIdentifierToRange()
        when (val origin = highlightingContext.overlayOriginContainer.data(node)) {
            is FromOverlay,
            is FromUnderlay -> mapOf(ownIdToRange)

            is MergedElements -> mapOf(ownIdToRange, origin.underlayElement.sourceIdentifierToRange())
            is ShadowedProperty -> mapOf(ownIdToRange, origin.underlayProperty.sourceIdentifierToRange())
        }
    }
}
