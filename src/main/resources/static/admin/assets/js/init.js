function Admin() {

    var _this = this;
    SEPARATOR_CURRENCY = ',';
    SEPARATOR_NUMBER = '.';

    this.submitForm = function($form, callback) {
        _this.resetForm($form);

        if (!_this.validateForm($form)) {
            return false;
        }

        _this.convertForm($form);

        $.ajax({
            url: $form.attr('action'),
            method: 'post',
            data: $form.serialize()
        }).then(function(data) {
            _this.showPopupNotice('변경 되었습니다.', function() {
                if (callback) callback();
                else location.reload();
            });
        }, function(jqXHR) {
            _this.resetForm($form);
            // show message error
            if (jqXHR.status == 400) {
                var response = JSON.parse(jqXHR.responseText);
                $.each(response.errors, function(key, value) {
                    _this.showErrorForm($form, key, value);
                });
            }
            // undefined error, show popup
            else {
                _this.showPopupNotice('변경하지 못 했습니다.');
            }
        });
    };

    this.submitFormImage = function($form, callback) {
        _this.resetForm($form);

        if (!_this.validateForm($form)) {
            return false;
        }

        _this.convertForm($form);
        var formData = new FormData($form[0]);
        console.log("data:"+formData);
        $.ajax({
            url: $form.attr('action'),
            type: 'POST',
            data: formData,
            cache: false,
            contentType: false,
            processData: false
        }).then(function(data) {
            _this.showPopupNotice('image upload success.', function() {
                if (callback) callback();
                else location.reload();
            });
        }, function(jqXHR) {
            _this.resetForm($form);
            // show message error
            if (jqXHR.status == 400) {
                var response = JSON.parse(jqXHR.responseText);
                $.each(response.errors, function(key, value) {
                    _this.showErrorForm($form, key, value);
                });
            }
            // undefined error, show popup
            else {
                _this.showPopupNotice('변경하지 못 했습니다.');
            }
        });
    };

    this.submitFormEstateWeb = function($form, callback) {
        _this.resetForm($form);

        if (!_this.validateForm($form)) {
            return false;
        }
        _this.convertFormWeb($form);
        var formData = new FormData($form[0]);
        console.log("data:"+formData);
        $.ajax({
            url: $form.attr('action'),
            type: 'POST',
            data: formData,
            cache: false,
            contentType: false,
            processData: false
        }).then(function(data) {
            if (callback)
                callback();
            else location.href='/member/estate/estate-waiting';

        }, function(jqXHR) {
            _this.resetForm($form);
            // show message error
            if (jqXHR.status == 400) {
                var response = JSON.parse(jqXHR.responseText);
                $.each(response.errors, function(key, value) {
                    _this.showErrorForm($form, key, value);
                });
            }
            // undefined error, show popup
            else {
                _this.showPopupNotice('변경하지 못 했습니다.');
            }
        });
    };
    this.submitFormEmailSend = function($form, callback) {
        _this.resetForm($form);

        if (!_this.validateForm($form)) {
            return false;
        }

        _this.convertForm($form);
        var formData = new FormData($form[0]);
        console.log("data:"+formData);
        $.ajax({
            url: $form.attr('action'),
            type: 'post',
            data: formData,
            cache: false,
            contentType: false,
            processData: false
        }).then(function(data) {
            _this.showPopupNotice('이메일 발송에 성공하였습니다.', function() {
                if (callback) callback();
                else location.reload();
            });
        }, function(jqXHR) {
            _this.resetForm($form);
            // show message error
            if (jqXHR.status == 400) {
                var response = JSON.parse(jqXHR.responseText);
                $.each(response.errors, function(key, value) {
                    _this.showErrorForm($form, key, value);
                });
            }
            // undefined error, show popup
            else {
                _this.showPopupNotice('변경하지 못 했습니다.');
            }
        });
    };

    this.submitFormEstate = function($form, callback) {
        _this.resetForm($form);

        if (!_this.validateForm($form)) {
            return false;
        }

        _this.convertForm($form);

        $.ajax({
            url: $form.attr('action'),
            method: 'post',
            data: $form.serialize()
        }).then(function(data) {
            _this.showPopupNotice('변경 되었습니다.', function() {
                // if (callback) callback();
                // else location.reload();
            });
        }, function(jqXHR) {
            _this.resetForm($form);
            // show message error
            if (jqXHR.status == 400) {
                var response = JSON.parse(jqXHR.responseText);
                $.each(response.errors, function(key, value) {
                    _this.showErrorForm($form, key, value);
                });
            }
            // undefined error, show popup
            else {
                _this.showPopupNotice('변경하지 못 했습니다.');
            }
        });
    };

    this.submitFormReport = function($form, callback) {
        _this.resetForm($form);

        if (!_this.validateForm($form)) {
            return false;
        }

        _this.convertForm($form);

        $.ajax({
            url: $form.attr('action'),
            method: 'post',
            data: $form.serialize()
        }).then(function(data) {
            _this.showPopupNotice('“이메일 발송에 성공하였습니다.', function() {
                if (callback) callback();
                else location.reload();
            });
        }, function(jqXHR) {
            _this.resetForm($form);
            // show message error
            if (jqXHR.status == 400) {
                var response = JSON.parse(jqXHR.responseText);
                $.each(response.errors, function(key, value) {
                    _this.showErrorForm($form, key, value);
                });
            }
            // undefined error, show popup
            else {
                _this.showPopupNotice('변경하지 못 했습니다.');
            }
        });
    };

    this.validateForm = function($form) {

        var result = true;
        var REGEX_EMAIL = /\S+@\S+\.\S+/;

        $form.find("input[required='required']").each(function(idx, elem) {
            if ($(this).val() == '') {
                _this.showErrorForm($form, $(this).attr('name'), '모든 항목을 입력해 주세요.');
                result = false;
            }
        });
        $form.find("input[type='email']").each(function(idx, elem) {
            console.log('mail: ' + $(this).val())
            if (!REGEX_EMAIL.test($(this).val())) {
                _this.showErrorForm($form, $(this).attr('name'), 'Email invalid');
                result = false;
            }
        });

        return result;
    };

    this.resetForm = function($form) {
        $form.find('.form-group').removeClass('has-error');
        $form.find('.help-block').remove();
    };

    this.showErrorForm = function($form, input, msg) {
        var $inp = $form.find("input[name='" + input + "']");
        var $inpDiv = $inp.closest('div.form-group');

        $inp.parent().append('<span class="help-block">' + msg + '</span>');
        $inpDiv.addClass('has-error');
    };

    this.convertForm = function($form) {
        // convert String to timestamp
        console.log("go convert form");
        $form.find('.date-picker').each(function(idx, elem) {
            var dateVal = $(this).val();
            var momentObj = moment(dateVal, 'M/D/YYYY');
            if (momentObj.isValid()) {
                $(this).val(momentObj.valueOf());
            } else {
                // clear invalid value
                $(this).val('');
            }
        });
        $form.find('.data-currency').each(function(idx, elem) {
            $(this).val($(this).val().replace(/,/g, ''));
        });
        $form.find('.data-number').each(function(idx, elem) {
            $(this).val($(this).val().replace(/\./g, ''));
        });
    };

    this.convertFormWeb = function($form) {
        // convert String to timestamp
        console.log("go convert form");
        $form.find('.date-picker').each(function(idx, elem) {
            var dateVal = $(this).val();
            var momentObj = moment(dateVal, 'YYYY/M/D');
            if (momentObj.isValid()) {
                $(this).val(momentObj.valueOf());
            } else {
                // clear invalid value
                $(this).val('');
            }
        });
        $form.find('.data-currency').each(function(idx, elem) {
            $(this).val($(this).val().replace(/,/g, ''));
        });
        $form.find('.data-number').each(function(idx, elem) {
            $(this).val($(this).val().replace(/\./g, ''));
        });
    };

    this.switchSelect = function($select, defaultValue) {
        if (defaultValue) {
            $select.val(defaultValue);
        }
        var selectValue = $select.val();
        var $input = $select.parent().next().find('.inp-search');

        $input.attr('name', selectValue);
        console.log("select value: "+selectValue);

        switch(selectValue) {
            case 'date':
                $input
                    .addClass('date-picker')
                    .datepicker({
                      rtl: Metronic.isRTL(),
                      orientation: "left",
                      autoclose: true,
                      language: 'kr'
                    });;
                break;
            default:
                $input
                    .removeClass('date-picker')
                    .datepicker('remove');
                break;
        }
    };


    this.populateFilter = function() {
        console.log("populate filter");
        var match,
            pl     = /\+/g,
            search = /([^&=]+)=?([^&]*)/g,
            decode = function (s) { return decodeURIComponent(s.replace(pl, " ")); },
            query  = window.location.search.substring(1),
            $form = $('.form-search');

        var urlParams = {};
        while (match = search.exec(query)) {
           urlParams[decode(match[1])] = decode(match[2]);
        }
        $.each(urlParams, function(key, value) {
            console.log("key: "+key);
            console.log("value: "+value);
            $form.find('.sel-nav').each(function() {
                var $select = $(this);
                $(this).find('option').each(function() {
                    if($(this).val() == key || $(this).attr('data-type') == key) {
                        admin.switchSelect($select, key);
                    }

                });
                if (key == 'date') {
                    $form.find("input[name='" + key + "']").val(
                        moment(parseInt(value)).format('M/D/YYYY')
                    );
                } else {
                    $form.find("input[name='" + key + "'], input[name='"+key+"']").val(value).val(value);
                }
            });
        });
    };

    this.deleteItem = function(url, callback) {
        console.log("url delete: "+url);
        $.ajax({
            url: url,
            method: 'delete'
        }).then(function(data) {
            // reload
            _this.showPopupNotice('변경 되었습니다.', function() {
                if (callback) callback();
                else location.reload();
            });
        }, function(jqXHR) {
            // show popup failed
            _this.showPopupNotice('변경하지 못 했습니다.');
        });
    };

    this.updateStatus = function(url, callback) {
        console.log("url put: "+url);
        $.ajax({
            url: url,
            method: 'put'
        }).then(function(data) {
            // reload
            _this.showPopupNotice('변경 되었습니다.', function() {
                if (callback) callback();
                else location.reload();
            });
        }, function(jqXHR) {
            // show popup failed
            _this.showPopupNotice('변경하지 못 했습니다.');
        });
    };

    this.showPopupConfirm = function(message, callback) {
        var $popupConfirm = $('#popup-confirm');
        if (message != null) {
            $popupConfirm.find('.popup-msg').html(message);

        }
        $popupConfirm.modal('toggle');
        if (callback) {
            $popupConfirm.find('#btn-confirm-action').on('click', function() {
                $popupConfirm.modal('hide');
                callback();
            });
        } else {
            // dismiss popup
            $popupConfirm.find('#btn-action').on('click', function() {
                $popupConfirm.modal('hide');
            });
        }
    };

    this.showPopupNotice = function(message, callback) {
        var $popupNotice = $('#popup-notice');
        if (message != null) {
            $popupNotice.find('.popup-msg').html(message);
        }
        $popupNotice.modal('toggle');
        if (callback) {
            $popupNotice.find('#btn-notice-action').on('click', function() {
                callback();
            });
        }
    };

    this.formatNumber = function(valueToFormat, separation) {
        // remove non digit
        var input_value = valueToFormat.replace(/\D/g, '');
        var input_length = input_value.length;
        if (input_length < 3) {
            return input_value;
        } else {
            var modulus = input_value.length % 3;
            if (modulus == 0) {
                return _this.format(input_value, separation);
            } else {
                var head = input_value.substring(0, modulus);
                var tail = input_value.substring(modulus, input_length);
                return head + separation + _this.format(tail, separation)
            }
        }
    };

    this.format = function(value, separation) {
        return value.match(/\d{3}/g).join(separation);
    };

    this.handleImgError = function() {
        $('img').one('error', function() { this.src = '../../static/admin/assets/images/no_image.jpg'; });
    };

}

var admin = new Admin();

$(function() {
    $(document).ajaxStart(function() {
        $('#loading-layer').show();
    });

    $(document).ajaxComplete(function() {
        $('#loading-layer').hide();
        admin.handleImgError();
    });

    $('.form-ajax').on('submit', function(evt) {
        console.log("form ajax");
        evt.preventDefault();
        admin.submitForm($(this));
    });

    $('.form-search').on('submit', function(evt) {
        console.log("go form submit");
        admin.convertForm($(this));
    });


    $('.th-content').on('click', '.btn-action-delete', function(evt) {
        var url = $(this).attr('data-url');
        admin.showPopupConfirm(null, function() {
            admin.deleteItem(url);
        });
    });

    $(document).on('click', '.btn-action-estate-delete', function(evt) {
        var url = $(this).attr('data-url');

        console.log(url);

        admin.showPopupConfirm(null, function() {
            admin.deleteItem(url);
        });
    });

    $('.form-search .sel-nav').on('change', function(evt) {
        admin.switchSelect($(this));
    });



    $('.data-currency').on('keyup', function() {
        var formatted_value = admin.formatNumber($(this).val(), SEPARATOR_CURRENCY);
        $(this).val(formatted_value);
    });

    $('.data-number').on('keyup', function() {
        var formatted_value = admin.formatNumber($(this).val(), SEPARATOR_NUMBER);
        $(this).val(formatted_value);
    });


    admin.populateFilter();
    admin.handleImgError();


});
