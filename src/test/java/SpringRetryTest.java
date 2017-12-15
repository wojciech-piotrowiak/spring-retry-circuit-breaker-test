import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.annotation.EnableRetry;

public class SpringRetryTest {

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            SpringRetryTest.TestConfiguration.class);
    ConnectionService connectionService = context.getBean(ConnectionService.class);

    @Test
    public void testCircuitBreaker() throws InterruptedException {

        incorrectStep();
        incorrectStep();
        incorrectStep();
        incorrectStep();
        incorrectStep();
        System.out.println();
        System.out.println();
        System.out.println();

        final long l = System.currentTimeMillis();
        correctStep(l);
        correctStep(l);
        correctStep(l);
        correctStep(l);
        correctStep(l);
        correctStep(l);
        correctStep(l);
        correctStep(l);
        correctStep(l);
        correctStep(l);
        correctStep(l);
        correctStep(l);
        correctStep(l);
        correctStep(l);
        correctStep(l);
        correctStep(l);
        correctStep(l);
        correctStep(l);
        correctStep(l);
        correctStep(l);
        correctStep(l);
        correctStep(l);

        //wait more than resetTimeout
        System.out.println();
        System.out.println();
        System.out.println();
        Thread.sleep(21_000L);
        correctStep(l);

    }

    private void incorrectStep() throws InterruptedException {
        doFailedUpload(connectionService);
        Thread.sleep(1_000L);
        System.out.println();
    }

    private void correctStep(final long l) throws InterruptedException {
        doCorrectUpload(connectionService);
        Thread.sleep(1_000L);
        printTime(l);
    }

    private void printTime(final long l) {
        System.out.println(String.format("%d ms after last failure", (System.currentTimeMillis() - l)));
    }

    private void doFailedUpload(ConnectionService externalService) throws InterruptedException {
        System.out.println("before fail");
        externalService.doUpload("FAIL");
        System.out.println("after fail");
        Thread.sleep(900);
    }

    private void doCorrectUpload(ConnectionService externalService) throws InterruptedException {
        System.out.println("before ok");
        externalService.doUpload("");
        System.out.println("after ok");
        Thread.sleep(900);
    }

    @Configuration
    @EnableRetry
    protected static class TestConfiguration {

        @Bean
        public ConnectionService externalService() {
            return new ConnectionService();
        }

        @Bean
        public RetryListener retryListener1() {
            return new RetryListener() {
                @Override
                public <T, E extends Throwable> boolean open(final RetryContext retryContext, final RetryCallback<T, E> retryCallback) {
                    System.out.println("----/ ---- open, retry count:" + retryContext.getRetryCount());
                    return true;
                }

                @Override
                public <T, E extends Throwable> void close(final RetryContext retryContext, final RetryCallback<T, E> retryCallback, final Throwable throwable) {
                    System.out.println("---------- close, retry count:" + retryContext.getRetryCount());
                }

                @Override
                public <T, E extends Throwable> void onError(final RetryContext retryContext, final RetryCallback<T, E> retryCallback, final Throwable throwable) {
                    System.out.println("onError, retry count:" + retryContext.getRetryCount());
                }

            };
        }

    }

}
