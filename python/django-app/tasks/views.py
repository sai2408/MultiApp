import json

from django.http import HttpResponse, JsonResponse
from django.shortcuts import get_object_or_404, redirect, render
from django.views.decorators.csrf import csrf_exempt
from django.views.decorators.http import require_POST

from .models import Task


# ---------- server-rendered web UI ----------

def index(request):
    return render(request, "tasks/index.html", {"tasks": Task.objects.all()})


@require_POST
def add(request):
    title = request.POST.get("title", "").strip()
    if title:
        Task.objects.create(title=title)
    return redirect("index")


@require_POST
def toggle(request, task_id):
    task = get_object_or_404(Task, pk=task_id)
    task.done = not task.done
    task.save()
    return redirect("index")


@require_POST
def delete(request, task_id):
    Task.objects.filter(pk=task_id).delete()
    return redirect("index")


# ---------- REST API (shared contract) ----------

def _json_body(request):
    try:
        return json.loads(request.body or b"{}")
    except json.JSONDecodeError:
        return {}


def _cors(response):
    response["Access-Control-Allow-Origin"] = "*"
    response["Access-Control-Allow-Headers"] = "Content-Type"
    response["Access-Control-Allow-Methods"] = "GET, POST, PUT, DELETE, OPTIONS"
    return response


@csrf_exempt
def api_tasks(request):
    if request.method == "GET":
        return _cors(JsonResponse([t.to_dict() for t in Task.objects.all()], safe=False))
    if request.method == "POST":
        title = str(_json_body(request).get("title", "")).strip()
        if not title:
            return _cors(JsonResponse({"error": "title is required"}, status=400))
        return _cors(JsonResponse(Task.objects.create(title=title).to_dict(), status=201))
    if request.method == "OPTIONS":
        return _cors(HttpResponse())
    return _cors(JsonResponse({"error": "method not allowed"}, status=405))


@csrf_exempt
def api_task(request, task_id):
    if request.method == "OPTIONS":
        return _cors(HttpResponse())
    task = Task.objects.filter(pk=task_id).first()
    if task is None:
        return _cors(JsonResponse({"error": "not found"}, status=404))
    if request.method == "PUT":
        data = _json_body(request)
        task.title = str(data.get("title", task.title)).strip() or task.title
        task.done = bool(data.get("done", task.done))
        task.save()
        return _cors(JsonResponse(task.to_dict()))
    if request.method == "DELETE":
        task.delete()
        return _cors(HttpResponse(status=204))
    return _cors(JsonResponse({"error": "method not allowed"}, status=405))
