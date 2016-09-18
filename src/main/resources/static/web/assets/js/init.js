function Utils() {
    this.serializeFormToJson = function($form) {
        var jsonObjectForm = {};
        $form.find('input').not("[type='submit']").not("[type='reset']").each(function() {
            jsonObjectForm[$(this).attr('name')] = $(this).val();
        });
        return jsonObjectForm;
    };
}

var utils = new Utils();