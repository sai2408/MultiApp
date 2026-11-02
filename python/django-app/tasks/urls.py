from django.urls import path

from . import views

urlpatterns = [
    # server-rendered web UI
    path("", views.index, name="index"),
    path("add/", views.add, name="add"),
    path("toggle/<int:task_id>/", views.toggle, name="toggle"),
    path("delete/<int:task_id>/", views.delete, name="delete"),
    # REST API (shared contract)
    path("api/tasks", views.api_tasks),
    path("api/tasks/<int:task_id>", views.api_task),
]
