public class Controller {
    Model model;
    View view;

    public Controller() {
        model = new Model();
        view = new View(model);
    }

    public void run() {
        view.printInput();                              // 파일명 입력 지시
        view.printAnalyzing(0);                  // xml 파일 분석중 출력
        model.setRoot(0, model.getFName()[0]);  // .xml 파일 dom 셋팅
        view.printAnalysisCompleted(0);          // xml 파일 분석 완료 출력
        view.printAnalyzing(1);                   // xsl 파일 분석 중 출력
        model.setRoot(1, model.getFName()[1]);   // .xsl 파일 dom 셋팅
        model.setPsMap(model.getRoot()[1]);            // HashMap에 Align 정보 저장
        model.setStyleMap(model.getRoot()[1]);         // HashMap에 Style 정보 저장
        model.setCsMap(model.getRoot()[1]);            // HashMap에 Size, Color, Shape 정보 저장
        model.setImgMap(model.getRoot()[1]);             // HashMap에 Src, Width, Height 정보 저장
        view.printAnalysisCompleted(1);          // xsl 파일 분석 완료 출력
        view.printCreateXml();                         // 최종 xml 문서 생성 중 출력
        model.createXML(model.getRoot()[2]);          // 최종 xml 문서 생성
        view.printCreateCompleted();                   // 최종 xml 문서 생성 완료 출력
    }
}