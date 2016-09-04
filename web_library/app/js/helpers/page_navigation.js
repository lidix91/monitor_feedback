define(["require", "exports", 'i18next', '../config', './../jquery.validate'], function (require, exports, i18n, config_1) {
    "use strict";
    var PageNavigation = (function () {
        function PageNavigation(configuration, container) {
            this.configuration = configuration;
            this.container = container;
        }
        PageNavigation.prototype.pageForwardCallback = function (currentPage, nextPage) {
            var textMechanisms = this.configuration.getMechanismConfig(config_1.mechanismTypes.textType);
            var ratingMechanisms = this.configuration.getMechanismConfig(config_1.mechanismTypes.ratingType);
            var screenshotMechanisms = this.configuration.getMechanismConfig(config_1.mechanismTypes.screenshotType);
            var categoryMechanisms = this.configuration.getMechanismConfig(config_1.mechanismTypes.categoryType);
            currentPage.find('.validate').each(function () {
                $(this).validate();
            });
            if (currentPage.find('.invalid').length > 0 && currentPage.find('.validate[data-mandatory-validate-on-skip="1"]').length > 0) {
                return false;
            }
            if (nextPage) {
                nextPage.find('.text-review').empty();
                nextPage.find('.rating-review').empty();
                nextPage.find('.screenshot-review').empty();
                for (var _i = 0, textMechanisms_1 = textMechanisms; _i < textMechanisms_1.length; _i++) {
                    var textMechanism = textMechanisms_1[_i];
                    if (textMechanism != null && nextPage.find('.text-review').length > 0 && textMechanism.active) {
                        var sectionSelector = "textMechanism" + textMechanism.id;
                        var textarea = currentPage.find('section#' + sectionSelector + ' textarea.text-type-text');
                        nextPage.find('.text-review').append('<p>' + textarea.val() + '</p>');
                    }
                }
                for (var _a = 0, ratingMechanisms_1 = ratingMechanisms; _a < ratingMechanisms_1.length; _a++) {
                    var ratingMechanism = ratingMechanisms_1[_a];
                    if (ratingMechanism != null && nextPage.find('.rating-review').length > 0 && ratingMechanism.active) {
                        nextPage.find('.rating-review').append(i18n.t('rating.review_title') + ": " + ratingMechanism.currentRatingValue + " / " + ratingMechanism.getParameterValue("maxRating"));
                    }
                }
                for (var _b = 0, screenshotMechanisms_1 = screenshotMechanisms; _b < screenshotMechanisms_1.length; _b++) {
                    var screenshotMechanism = screenshotMechanisms_1[_b];
                    if (screenshotMechanism != null && nextPage.find('.screenshot-review').length > 0 && screenshotMechanism.active &&
                        screenshotMechanism.screenshotView !== undefined && screenshotMechanism.screenshotView.screenshotCanvas !== undefined && screenshotMechanism.screenshotView.screenshotCanvas !== null) {
                        var img = $('<img src="' + screenshotMechanism.screenshotView.screenshotCanvas.toDataURL() + '" />');
                        img.css('width', '40%');
                        img.addClass('base');
                        var screenshotReviewElement = nextPage.find('.screenshot-review');
                        screenshotReviewElement.append(img);
                        var screenshotTextReview = $('<p class="screenshot-text-review"></p>');
                        screenshotReviewElement.append(screenshotTextReview);
                        var ratio = 965 * 0.4 / screenshotMechanism.screenshotView.screenshotPreviewElement.width();
                        jQuery('section#screenshotMechanism' + screenshotMechanism.id + ' .sticker-container').each(function () {
                            var x = $(this).position().left;
                            var y = $(this).position().top;
                            var width = $(this).width();
                            var height = $(this).height();
                            var reviewClone = $(this).clone();
                            reviewClone.removeClass('ui-resizable');
                            reviewClone.removeClass('ui-draggable');
                            reviewClone.removeClass('ui-draggable-handle');
                            reviewClone.css('left', x * ratio + "px");
                            reviewClone.css('top', y * ratio + "px");
                            reviewClone.css('width', width * ratio + "px");
                            reviewClone.css('height', height * ratio + "px");
                            reviewClone.css('padding', "0");
                            reviewClone.addClass('sticker-container-review');
                            reviewClone.find('a').remove();
                            if ($(this).hasClass('text-2')) {
                                var textarea = $(this).find('textarea');
                                var text = textarea.val();
                                var oldFontSize = textarea.css('font-size');
                                var newFontSize = ratio * parseInt(oldFontSize);
                                reviewClone.find('textarea').val(text).css('font-size', newFontSize + 'px').prop("disabled", true);
                            }
                            if ($(this).hasClass('text') || $(this).hasClass('text-2')) {
                                var text = $(this).find('textarea').val();
                                reviewClone.on('mouseover mouseenter', function () {
                                    $('.screenshot-text-review').text(text).css('display', 'inline');
                                }).on('mouseleave', function () {
                                    $('.screenshot-text-review').text("").css('display', 'none');
                                });
                            }
                            screenshotReviewElement.append(reviewClone);
                        });
                    }
                }
                for (var _c = 0, categoryMechanisms_1 = categoryMechanisms; _c < categoryMechanisms_1.length; _c++) {
                    var categoryMechanism = categoryMechanisms_1[_c];
                    if (categoryMechanism !== null && nextPage.find('.category-review').length > 0 && categoryMechanism.active) {
                        var inputSelector = 'section#categoryMechanism' + categoryMechanism.id + '.category-type input';
                        currentPage.find(inputSelector).each(function () {
                            var input = $(this);
                            var selector = 'input#review' + input.attr('id');
                            var correspondingReviewInput = nextPage.find(selector);
                            correspondingReviewInput.prop("checked", input.is(':checked'));
                        });
                    }
                }
            }
            return true;
        };
        return PageNavigation;
    }());
    exports.PageNavigation = PageNavigation;
});
//# sourceMappingURL=page_navigation.js.map