// Do not edit this file but the files in the configurations/ folder

export const defaultSuccessMessage = 'Your feedback was successfully sent';
export const dialogOptions = {
    autoOpen: false,
    height: 'auto',
    width: 'auto',
    minWidth: 500,
    modal: true,
    title: 'Feedback',
    buttons: {},
    resizable: false
};
export const mechanismTypes = {
    textType: 'TEXT_TYPE',
    ratingType: 'RATING_TYPE',
    screenshotType: 'SCREENSHOT_TYPE',
    audioType: 'AUDIO_TYPE',
    categoryType: 'CATEGORY_TYPE',
    attachmentType: 'ATTACHMENT_TYPE'
};
export const configurationTypes = {
    push: 'PUSH',
    pull: 'PULL'
};
export const cookieNames = {
    lastTriggered: 'lastTriggered'
};
export const apiEndpointOrchestrator = 'http://supersede.es.atos.net:8081/';
export const applicationPath = 'orchestrator/feedback/{lang}/applications/';
export const apiEndpointRepository = 'http://supersede.es.atos.net:8081/';
export const feedbackPath = "feedback_repository/{lang}/applications/{applicationId}/feedbacks";
export const applicationId = 14;
export const applicationName = 'ATOS';
export const feedbackObjectTitle = 'Feedback ATOS';
