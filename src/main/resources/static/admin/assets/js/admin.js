$(document).ready(function() {
  /* --------------------------------------------------------------------- */
  /* Metronic
  /* --------------------------------------------------------------------- */
  (function($) {
    if ($("body.login-page").length) return;

    Metronic.init(); // init metronic core components
    Layout.init(); // init current layout
    Demo.init(); // init demo features
  })(jQuery);



  /* --------------------------------------------------------------------- */
  /* data-sb-placeholder
  /* --------------------------------------------------------------------- */
  (function($) {
    if (!$("[data-sb-placeholder]").length) return;

    $("[data-sb-placeholder]").each(function() {
      var placeholderContent = $(this).attr("data-sb-placeholder");
      $(this).attr("placeholder", placeholderContent);

      $(this).on("focus", function() {
        $(this).attr("placeholder", "");
      });

      $(this).on("blur", function() {
        $(this).attr("placeholder", placeholderContent);
      });
    });
  })(jQuery);



  /* --------------------------------------------------------------------- */
  /* .date-picker
  /* --------------------------------------------------------------------- */
  (function($) {
    if (!$(".date-picker").length) return;

    $('.date-picker').datepicker({
      rtl: Metronic.isRTL(),
      orientation: "left",
      autoclose: true,
      language: 'kr'
    });
  })(jQuery);



  /* --------------------------------------------------------------------- */
  /* .ckeditor
  /* --------------------------------------------------------------------- */
  (function($) {
    if (!$(".ckeditor").length) return;

    var idNum = 1;

    $(".ckeditor").each(function() {
      $(this).attr("id", "sb-ckeditor" + idNum);
      var id = $(this).attr("id");

      $("" + id + "").ckeditor({
        language: "ko"
      });

      idNum++;
    });
  })(jQuery);



  /* --------------------------------------------------------------------- */
  /* .input-hashtag
  /* --------------------------------------------------------------------- */
  (function($) {
    if (!$(".input-hashtag").length) return;

    $(".input-hashtag").select2({
      tags: ["webdesign", "pictures", "test"]
    });
  })(jQuery);



  /* --------------------------------------------------------------------- */
  /* spinner
  /* --------------------------------------------------------------------- */
  (function($) {
    if (!$('.spinner').length) return;

    $('#spinner1').spinner();

    $('#spinner2').spinner({
      disabled: true
    });

    $('#spinner3').spinner({
      value: 0,
      min: 0,
      max: 10
    });

    $('#spinner4').spinner({
      value: 0,
      step: 5,
      min: 0,
      max: 200
    });
  })(jQuery);



  /* --------------------------------------------------------------------- */
  /* touchspin
  /* --------------------------------------------------------------------- */
  (function($) {
    if (!$('.touchspin').length) return;

    $("#touchspin_demo1").TouchSpin({
      buttondown_class: 'btn green',
      buttonup_class: 'btn green',
      min: -1000000000,
      max: 1000000000,
      stepinterval: 50,
      maxboostedstep: 10000000,
      prefix: '$'
    });

    $("#touchspin_demo2").TouchSpin({
      buttondown_class: 'btn blue',
      buttonup_class: 'btn blue',
      min: 0,
      max: 100,
      step: 0.1,
      decimals: 2,
      boostat: 5,
      maxboostedstep: 10,
      postfix: '%'
    });
  })(jQuery);



  /* --------------------------------------------------------------------- */
  /* .textarea-autosize
  /* --------------------------------------------------------------------- */
  (function($) {
    if (!$('.textarea-autosize').length) return;

    $('.textarea-autosize').each(function() {
      var textarea = $(this);
      autosize(textarea);
      textarea.css('height', '');

      textarea.on('click keyup focusout', function() {
        textarea.val() == '' ? textarea.css('height', '') : null
      });
    });
  })(jQuery);



  /* --------------------------------------------------------------------- */
  /* Create preview image upload
  /* --------------------------------------------------------------------- */
  (function($) {
    if (!$('.sb-file-upload.image-upload').length) return;

    $('.sb-file-upload.image-upload').each(function() {
      var fileUpload = $(this);
      var previewImage = fileUpload.find('.preview-image > img');
      var previewImageBg = fileUpload.find('.preview-image > .image-bg');

      previewImage.attr('src') == '' ? previewImage.closest('.preview-image').addClass('no-image') : previewImage.closest('.preview-image').removeClass('no-image');
      previewImageBg.attr('style') == undefined ? previewImageBg.closest('.preview-image').addClass('no-image') : previewImageBg.closest('.preview-image').removeClass('no-image');

      var readURL = function(input) {
        if (input.files && input.files[0]) {
          var reader = new FileReader();

          reader.onload = function(e) {
            previewImage.attr('src', e.target.result);
            previewImageBg.attr('style', 'background-image: url("' + e.target.result + '");');
          }

          reader.readAsDataURL(input.files[0]);
        }
      };

      fileUpload.find('.input-file').on('change', function() {
        readURL(this);
      });
    });
  })(jQuery);



  /* --------------------------------------------------------------------- */
  /* Check valid file upload
  /* --------------------------------------------------------------------- */
  (function($) {
    if (!$('.sb-file-upload').length) return;

    var popupUploadAgain = $('#popupUploadAgain');

    $('.sb-file-upload').each(function() {
      var fileUploadWrapper = $(this);
      var inputFile = fileUploadWrapper.find('.input-file');
      var borderBox = fileUploadWrapper.find('.border-box');
      var showFileInfo = fileUploadWrapper.find('.show-file-info');
      var previewImage = fileUploadWrapper.find('.preview-image');
      var deleteButton = fileUploadWrapper.find('.btn-delete');

      inputFile.on('change', function() {
        var thisInputFile = $(this);
        var file = thisInputFile[0].files[0];
        var fileName = file.name;
        var fileSize = file.size;

        showFileInfo.empty().text(fileName);

        if ($('.sb-file-upload.image-upload').length) {
          var capacityLimit = 21 * 1024 * 1024; // = 21MB
          var fileExtension = fileName.split('.').pop().toLowerCase();
          var listExtensionArray = ['jpg', 'jpeg', 'gif', 'png'];
          var inArrayExtension = $.inArray(fileExtension, listExtensionArray);

          if (inArrayExtension >= 0 && fileSize / capacityLimit <= 1) {
            thisInputFile.removeClass('input-empty').addClass('input-not-empty');
            borderBox.css('border', '');
            previewImage.removeClass('no-image');
          } else {
            thisInputFile.replaceWith(thisInputFile = thisInputFile.clone(true));
            thisInputFile.removeClass('input-not-empty').addClass('input-empty');
            borderBox.css('border', '1px solid #ff0000');
            previewImage.addClass('no-image');
            showFileInfo.empty();
            popupUploadAgain.modal('show');
          }
        }

        if (fileUploadWrapper.find('.input-file').val() === '') {
          thisInputFile.addClass('input-empty').removeClass('input-not-empty');
        } else {
          thisInputFile.addClass('input-not-empty').removeClass('input-empty');
        }
      });

      deleteButton.on('click', function() {
        inputFile.replaceWith(inputFile = inputFile.clone(true));
        inputFile.removeClass('input-not-empty').addClass('input-empty');
        borderBox.css('border', '');
        previewImage.addClass('no-image').find('> img').attr('src', '');
        previewImage.addClass('no-image').find('> .image-bg').attr('style', 'background-image: url();');
        showFileInfo.empty();
      });
    });
  })(jQuery);


  /* --------------------------------------------------------------------- */
  /* #broker-detail-slider
  /* --------------------------------------------------------------------- */
  (function($) {
    if (!$('#broker-detail-slider').length) return;

    var galleryTop = $('#broker-detail-slider .gallery-top');
    var galleryThumbs = $('#broker-detail-slider .gallery-thumbs');

    galleryTop.owlCarousel({
      singleItem: true,
      slideSpeed: 600,
      autoPlay: 6000,
      addClassActive: true,
      stopOnHover: true,
      responsive: true,
      responsiveRefreshRate: 200,
      navigation: true,
      navigationText: [
        '<img class="owl-button-img" src="../../assets/images/icon/icon_angle_left.png" alt="icon">',
        '<img class="owl-button-img" src="../../assets/images/icon/icon_angle_right.png" alt="icon">'
      ],
      rewindNav: false,
      pagination: false,

      afterAction: function() {
        var _this = this;
        syncPosition(_this);
        currentPerTotal(_this);
      }
    });

    galleryThumbs.owlCarousel({
      items: 5,
      addClassActive: true,
      pagination: false,
      responsive: true,
      responsiveRefreshRate: 100,

      afterInit: function(el) {
        el.find(".owl-item").eq(0).addClass("current");
      }
    });

    function syncPosition(_this) {
      var current = _this.currentItem;
      $("#broker-detail-slider .gallery-thumbs").find(".owl-item").removeClass("current").eq(current).addClass("current");
      $("#broker-detail-slider .gallery-thumbs").data("owlCarousel") !== undefined ? center(current) : null;
    }

    $("#broker-detail-slider .gallery-thumbs").on("click", ".owl-item", function(e) {
      e.preventDefault();
      var number = $(this).data("owlItem");
      galleryTop.trigger("owl.goTo", number);
    });

    function center(number) {
      var galleryThumbsVisible = galleryThumbs.data("owlCarousel").owl.visibleItems;
      var num = number;
      var found = false;

      for (var i in galleryThumbsVisible) {
        if (num === galleryThumbsVisible[i]) {
          var found = true;
        }
      }

      if (found === false) {
        if (num > galleryThumbsVisible[galleryThumbsVisible.length - 1]) {
          galleryThumbs.trigger("owl.goTo", num - galleryThumbsVisible.length + 2)
        } else {
          if (num - 1 === -1) {
            num = 0;
          }
          galleryThumbs.trigger("owl.goTo", num);
        }
      } else if (num === galleryThumbsVisible[galleryThumbsVisible.length - 1]) {
        galleryThumbs.trigger("owl.goTo", galleryThumbsVisible[1])
      } else if (num === galleryThumbsVisible[0]) {
        galleryThumbs.trigger("owl.goTo", num - 1)
      }
    }

    function currentPerTotal(_this) {
      var currentNum = _this.currentItem + 1;
      var totalNum = _this.itemsAmount;

      $('#broker-detail-slider .current-per-total').text(currentNum + '/' + totalNum);
    }
  })(jQuery);



  /* --------------------------------------------------------------------- */
  /* validation form_sample_3
  /* --------------------------------------------------------------------- */
  (function($) {
    if (!$(".form_sample_3").length) return;

    var form3 = $('.form_sample_3');
    var error3 = $('.alert-danger', form3);
    var success3 = $('.alert-success', form3);

    //IMPORTANT: update CKEDITOR textarea with actual content before submit
    form3.on('submit', function() {
      for (var instanceName in CKEDITOR.instances) {
        CKEDITOR.instances[instanceName].updateElement();
      }
    });

    form3.validate({
      errorElement: 'span', //default input error message container
      errorClass: 'help-block help-block-error', // default input error message class
      focusInvalid: false, // do not focus the last invalid input
      ignore: "", // validate all fields including form hidden input
      rules: {
        nameABC: {
          minlength: 2,
          required: true
        },
        email: {
          required: true,
          email: true
        },
        options1: {
          required: true
        },
        options2: {
          required: true
        },
        select2tags: {
          required: true
        },
        datepicker: {
          required: true
        },
        occupation: {
          minlength: 5,
        },
        membership: {
          required: true
        },
        service: {
          required: true,
          minlength: 2
        },
        editor2: {
          required: true
        }
      },

      messages: { // custom messages for radio buttons and checkboxes
        membership: {
          required: "Please select a Membership type"
        },
        service: {
          required: "Please select  at least 2 types of Service",
          minlength: jQuery.validator.format("Please select  at least {0} types of Service")
        }
      },

      errorPlacement: function(error, element) { // render error placement for each input type
        if (element.parent(".input-group").size() > 0) {
          error.insertAfter(element.parent(".input-group"));
        } else if (element.attr("data-error-container")) {
          error.appendTo(element.attr("data-error-container"));
        } else if (element.parents('.radio-list').size() > 0) {
          error.appendTo(element.parents('.radio-list').attr("data-error-container"));
        } else if (element.parents('.radio-inline').size() > 0) {
          error.appendTo(element.parents('.radio-inline').attr("data-error-container"));
        } else if (element.parents('.checkbox-list').size() > 0) {
          error.appendTo(element.parents('.checkbox-list').attr("data-error-container"));
        } else if (element.parents('.checkbox-inline').size() > 0) {
          error.appendTo(element.parents('.checkbox-inline').attr("data-error-container"));
        } else {
          error.insertAfter(element); // for other inputs, just perform default behavior
        }
      },

      invalidHandler: function(event, validator) { //display error alert on form submit   
        success3.hide();
        error3.show();
        Metronic.scrollTo(error3, -200);
      },

      highlight: function(element) { // hightlight error inputs
        $(element)
          .closest('.form-group').addClass('has-error'); // set error class to the control group
      },

      unhighlight: function(element) { // revert the change done by hightlight
        $(element)
          .closest('.form-group').removeClass('has-error'); // set error class to the control group
      },

      success: function(label) {
        label
          .closest('.form-group').removeClass('has-error'); // set success class to the control group
      },

      submitHandler: function(form) {
        success3.show();
        error3.hide();
        form[0].submit(); // submit the form
      }
    });

    //apply validation on select2 dropdown value change, this only needed for chosen dropdown integration.
    $('.select2me', form3).change(function() {
      form3.validate().element($(this)); //revalidate the chosen dropdown value and show error or success message for the input
    });

    // initialize select2 tags
    $("#select2_tags").change(function() {
      form3.validate().element($(this)); //revalidate the chosen dropdown value and show error or success message for the input 
    }).select2({
      tags: ["red", "green", "blue", "yellow", "pink"]
    });

    $('.date-picker .form-control').change(function() {
      form3.validate().element($(this)); //revalidate the chosen dropdown value and show error or success message for the input 
    });

    
  })(jQuery);

 /* --------------------------------------------------------------------- */
  /* validation form_sample_3
  /* --------------------------------------------------------------------- */
  $("#owl-demo").owlCarousel({
    items : 8,
    lazyLoad : true,
    navigation : true
  });
  $("#owl-demo2").owlCarousel({
    items : 8,
    lazyLoad : true,
    navigation : true
  });

  $(function() {

    var clicks = 0;
    var timer = null;

    $(".btn-send-mail").on("click", function(e) {
      var page = $(this).attr('data-page');
      if(clicks != page) {
        var curent_url = window.location.pathname;
        var current_page = curent_url.split('/');
        var page_num = current_page[current_page.length -1];
        if(page_num != page) {
          window.location = '/admin/main/email/'+page;
        }
        clicks = page;
      }else {
        if(page == 1) {
          // $("input[name='type']").val('SignUpToday');
          $("input[name='type']").attr('readonly','readonly');
        }
        if(page ==2) {
          // $("input[name='type']").val('SignUpLastMonth');
          $("input[name='type']").attr('readonly','readonly');
        }
        if(page ==3) {
          // $("input[name='type']").val('RegularMember');
          $("input[name='type']").attr('readonly','readonly');
        }
        if(page ==4) {
          // $("input[name='type']").val('Broker');
          $("input[name='type']").attr('readonly','readonly');
        }
        if(page ==5) {
          // $("input[name='type']").val('TrustMember');
          $("input[name='type']").attr('readonly','readonly');
        }
        if(page ==6) {
          // $("input[name='type']").val('AllMember');
          $("input[name='type']").attr('readonly','readonly');
        }
      }
    });

    $(".btn-send-mail").dblclick(function(){
      window.location = '/admin/main/email/0';
      var page = $(this).attr('data-page');
      console.log(page);
      if(page ==1) {
        var recipient = $("input[name='type']");
        if(recipient.attr('readonly').length) {
          $("input[name='type']").val('');
          $("input[name='type']").removeAttr('readonly');
        }
      }
      if(page ==2) {
        var recipient = $("input[name='type']");
        if(recipient.attr('readonly').length) {
          $("input[name='type']").val('');
          $("input[name='type']").removeAttr('readonly');
        }
      }
      if(page ==3) {
        var recipient = $("input[name='type']");
        if(recipient.attr('readonly').length) {
          $("input[name='type']").val('');
          $("input[name='type']").removeAttr('readonly');
        }
      }
      if(page ==4) {
        var recipient = $("input[name='type']");
        if(recipient.attr('readonly').length) {
          $("input[name='type']").val('');
          $("input[name='type']").removeAttr('readonly');
        }
      }
      if(page ==5) {
        var recipient = $("input[name='type']");
        if(recipient.attr('readonly').length) {
          $("input[name='type']").val('');
          $("input[name='type']").removeAttr('readonly');
        }
      }
      if(page ==6) {
        var recipient = $("input[name='type']");
        if(recipient.attr('readonly').length) {
          $("input[name='type']").val('');
          $("input[name='type']").removeAttr('readonly');
        }
      }
    });

    $("#clickable").on("dblclick", function(e) {
      e.preventDefault(); //Prevent double click
    });

  });
});

