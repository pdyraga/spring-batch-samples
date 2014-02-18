<html>

  <head>
    <title>Spring Batch Samples - scalability</title>

    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>

    <script type="text/javascript">

      $(document).ready(function() {

        $('#elixir0Generate').submit(function(ev) {
          var form = $(this);
          var submitButton = form.find('.button');

          var destinationFilePath = form.find('input[name=destinationFilePath]').val();

          clearResponse(form);
          submitButton.prop('value', 'Generating...').attr('disabled','disabled');

          $.ajax({
            type: 'POST',
            url: 'generate',
            data: form.serialize(),
            success: function(text) {
              submitButton.prop('value', 'Generate').removeAttr('disabled');
              $('#runImportJob input[name=inputFilePath]').val(destinationFilePath);
              showResponse('success', text, form);
            }
          });

          ev.preventDefault();
        });


        $('#runImportJob').submit(function(ev) {
          var form = $(this);
          var submitButton = form.find('.button');

          clearResponse(form);
          submitButton.prop('value', 'Running...').attr('disabled','disabled');

          $.ajax({
            type: 'POST',
            url: 'startJob',
            data: form.serialize(),
            success: function(text) {
              submitButton.prop('value', 'Run job').removeAttr('disabled');
              showResponse('success', text, form);
            }
          });
          ev.preventDefault();
        });
      });

      clearResponse = function(element) {
        var responseElementId = element.attr('id') + 'Response';
        var responseElement = $('#' + responseElementId);
        responseElement.fadeOut('slow', function() {
          $(this).remove();
        });
      };

      showResponse = function(type, text, element) {
        var responseElementId = element.attr('id') + 'Response';
        var responseElement = $('<span id="' + responseElementId + '" class="' + type + '" style="display:none">' + text + '</span>').insertAfter(element);
        responseElement.fadeIn('slow');
      };
    </script>
  </head>

  <body>
      <form id="elixir0Generate">
        Output file path: <input name="destinationFilePath" />
        Number of transactions: <input name="numberOfTransactions" />

        <input class="button" type="submit" value="Generate" />
      </form>

      <form id="runImportJob">
        Input file path: <input name="inputFilePath" />

        <input class="button" type="submit" value="Run job" />
      </form>
  </body>

</html>

