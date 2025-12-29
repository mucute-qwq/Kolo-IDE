package io.github.mucute.qwq.koloide.module.nodejs.template

import io.github.mucute.qwq.koloide.module.nodejs.R

internal data class ProjectTemplate(
    val nameResId: Int,
    val descriptionResId: Int,
    val path: String
) {

    companion object {

        val NormalTemplate = ProjectTemplate(
            nameResId = R.string.normal_template,
            descriptionResId = R.string.normal_template_description,
            path = "template/normal-template.zip"
        )

        val VueTemplate = ProjectTemplate(
            nameResId = R.string.vue_template,
            descriptionResId = R.string.vue_template_description,
            path = "template/vue-template.zip"
        )

        val ReactTemplate = ProjectTemplate(
            nameResId = R.string.react_template,
            descriptionResId = R.string.react_template_description,
            path = "template/react-template.zip"
        )

        val SvelteTemplate = ProjectTemplate(
            nameResId = R.string.svelte_template,
            descriptionResId = R.string.svelte_template_description,
            path = "template/svelte-template.zip"
        )

        val SolidTemplate = ProjectTemplate(
            nameResId = R.string.solid_template,
            descriptionResId = R.string.solid_template_description,
            path = "template/solid-template.zip"
        )

        val AngularTemplate = ProjectTemplate(
            nameResId = R.string.angular_template,
            descriptionResId = R.string.angular_template_description,
            path = "template/angular-template.zip"
        )

    }

}

internal val ProjectTemplates = with(ProjectTemplate) {
    listOf(
        NormalTemplate,
        VueTemplate,
        ReactTemplate,
        SvelteTemplate,
        SolidTemplate,
        AngularTemplate
    )
}